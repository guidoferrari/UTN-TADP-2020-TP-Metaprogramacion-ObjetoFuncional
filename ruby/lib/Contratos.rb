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
      @__precondiciones__ = Hash.new
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
      @__bloque_precondicion__ = bloque
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
        @__precondiciones__[method_name] = @__bloque_precondicion__ if @__bloque_precondicion__
        @__bloque_precondicion__ = nil
        precondiciones = @__precondiciones__

        puts "Redefiniendo el metodo #{method_name}."

        self.define_method(method_name) do |*args, &block|
          metodo = metodo_viejo.bind(self)
          parametros = metodo.parameters.map(&:last).map(&:to_s)
          puts 'define method'

          mapaArgumentos = parametros.zip(args)
          #puts *args
          precondicion = precondiciones[method_name]
          #lambda_con_parametros = ->(*parametros) {precondicion.call()}
          #lambda_con_parametros.call(*args) if precondiciones[method_name]

          #Proc.new {|*parametros| precondicion.call(*args) }.call()
          #Proc.new {|*parametros| precondicion.call() }.call(*args)

          #Struct.new(*parametros).new(*args).instance_exec(&precondicion)

          #algo = { "divisor" => 10 }
          #self.class.instance_exec **algo, &precondicion

          #self.instance_exec(argumentos, &precondiciones[method_name]) if precondiciones[method_name]
          #EjecutadorDeCondiciones.ejecutar_condicion(metodo, *args, 'precondition', precondiciones[method_name]) if precondiciones[method_name]

          #divisor = 10
          #Proc.new { divisor }.call
          #binding.eval(&precondicion)

          #puts metodo.get_binding
          puts binding.class
          metodo.instance_exec *mapaArgumentos, &precondicion

          self.instance_exec &ejecutarAntes if ejecutarAntes
          resultado = metodo.call(*args)
          self.instance_exec &ejecutarDespues if ejecutarDespues

          unless accesors.include? method_name.to_sym
            EjecutadorDeInvariante.ejecutar_invariantes(self, invariantes)
          end

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
end