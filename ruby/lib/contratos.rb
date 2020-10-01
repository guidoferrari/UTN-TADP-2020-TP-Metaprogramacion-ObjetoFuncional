require_relative 'ejecutador'

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
    end
  end

  module ClassMethods

    def method_added(method_name)
      __no_recursivo__ do

        accesors, ejecutarAntes, ejecutarDespues, invariantes, metodo_viejo, postcondicion, precondicion = guardar_variables_instancia(method_name)

        self.define_method(method_name) do |*args, &block|

          ejecutador = Ejecutador.new(metodo_viejo, self, *args)
          ejecutador.ejecutar_condicion('precondition', precondicion, nil) unless precondicion.nil?
          ejecutador.ejecutar(&ejecutarAntes) if ejecutarAntes
          resultado = ejecutador.ejecutarMetodo
          ejecutador.ejecutar(&ejecutarDespues) if ejecutarDespues
          ejecutador.ejecutar_invariantes(invariantes) unless accesors.include? method_name.to_sym
          ejecutador.ejecutar_condicion('postcondition', postcondicion, resultado) unless postcondicion.nil?
          resultado

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

    def before_and_after_each_call(procAntes, procDespues)
      @__antes_despues__ = AntesDespues.new(procAntes, procDespues)
    end

    def invariant(&bloque)
      @__invariantes__ << Invariante.new(bloque)
    end

    def pre(&bloque)
      @__precondition__ = Condition.new(bloque)
    end

    def post(&bloque)
      @__postcondition__ = Condition.new(bloque)
    end

    def attr_accessor(*args)
      @__accessors__ += args
      super
    end

    private

    def guardar_variables_instancia(method_name)
      metodo_viejo = self.instance_method(method_name)

      if @__antes_despues__
        ejecutarAntes = @__antes_despues__.antes
        ejecutarDespues = @__antes_despues__.despues
      end

      invariantes = @__invariantes__
      accesors = @__accessors__

      precondicion = @__precondition__
      @__precondition__ = nil

      postcondicion = @__postcondition__
      @__postcondition__ = nil
      return accesors, ejecutarAntes, ejecutarDespues, invariantes, metodo_viejo, postcondicion, precondicion
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

  class Condition
    attr_accessor :bloque

    def initialize(bloque)
      @bloque = Proc.new(&bloque)
    end
  end
end