require_relative 'EjecutadorDeInvariante'
require_relative 'EjecutadorDeCondiciones'

module Contratos
  def self.included(klass)
    inicializar_controlados(klass)
    if klass == Class
      klass.include ClassMethods
    else
      klass.extend ClassMethods
    end
  end

  def self.inicializar_controlados(klass)
    klass.instance_eval do
      @__invariantes__ = []
      @__accessors__ = []
      @__preconditions__ = []
    end
  end

  module ClassMethods

    def before_and_after_each_call(procAntes, procDespues)
      @__antes_despues__ = AntesDespues.new(procAntes, procDespues)
    end

    def invariant(&bloque)
      @__invariantes__ << Invariante.new(bloque)
    end

    def pre(&bloque)
      @__preconditions__.push Precondicion.new(bloque)
    end

    def post(&bloque)
      @post = Postcondicion.new(bloque)
    end

    def attr_accessor(*args)
      @__accessors__ += args
      super
    end

    def method_added(method_name)
      __no_recursivo__ do

        metodo_viejo = self.instance_method(method_name)

        if @__antes_despues__
          ejecutarAntes = @__antes_despues__.antes
          ejecutarDespues = @__antes_despues__.despues
        end

        invariantes = @__invariantes__
        accesors = @__accessors__
        precondiciones = @__preconditions__
        postcondicion = @pre

        puts "Redefiniendo el metodo #{method_name}."

        self.define_method(method_name) do |*args, &block|
          binded_method = metodo_viejo.bind(self)

          EjecutadorDeCondiciones.ejecutar_condicion(binded_method, *args, 'precondition', precondiciones.pop()) if precondiciones.first != nil

          self.instance_exec &ejecutarAntes if ejecutarAntes
          resultado = binded_method.call(*args)
          self.instance_exec &ejecutarDespues if ejecutarDespues

          unless accesors.include? method_name.to_sym
            EjecutadorDeInvariante.ejecutar_invariantes(self, invariantes)
          end

          resultado

          puts "FINISH redefiniendo #{method_name}."
        end
      end
    end

    def __no_recursivo__
      begin
        return if Thread.current[:__ejecutando__]

        Thread.current[:__ejecutando__] = true
        yield if block_given?

      ensure
        Thread.current[:__ejecutando__] = false
      end
    end
  end

  class AntesDespues
    attr_accessor :antes, :despues

    def initialize(antes, despues)
      @antes = antes
      @despues = despues
    end
  end

  class Invariante
    attr_accessor :bloque

    def initialize(bloque)
      @bloque = bloque
    end
  end

  class Precondicion
    attr_accessor :bloque

    def initialize(bloque)
      @bloque = bloque
    end
  end

  class Postcondicion
    attr_accessor :bloque

    def initialize(bloque)
      @bloque = bloque
    end
  end
end