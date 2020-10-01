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
      @__precondiciones__ = []
      @__postcondiciones__ = []
    end
  end

  module ClassMethods

    def method_added(method_name)
      __no_recursivo__ do

        accesors, ejecutarAntes, ejecutarDespues, invariantes, metodo_viejo, postcondiciones, precondiciones = guardar_variables_instancia(method_name)

        self.define_method(method_name) do |*args, &block|
          ejecutador = Ejecutador.new(metodo_viejo, self, *args)
          ejecutador.ejecutar_condiciones('precondition', precondiciones, nil)
          ejecutador.ejecutar(&ejecutarAntes) if ejecutarAntes
          resultado = ejecutador.ejecutarMetodo
          ejecutador.ejecutar(&ejecutarDespues) if ejecutarDespues
          ejecutador.ejecutar_invariantes(invariantes) unless accesors.include? method_name.to_sym
          ejecutador.ejecutar_condiciones('postcondition', postcondiciones, resultado)
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
      @__invariantes__ << Proc.new(&bloque)
    end

    def pre(&bloque)
      @__precondiciones__ << Proc.new(&bloque)
    end

    def post(&bloque)
      @__postcondiciones__ << Proc.new(&bloque)
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

      precondiciones = @__precondiciones__
      @__precondiciones__ = []

      postcondiciones = @__postcondiciones__
      @__postcondiciones__ = []

      return accesors, ejecutarAntes, ejecutarDespues, invariantes, metodo_viejo, postcondiciones, precondiciones
    end
  end

  class AntesDespues
    attr_accessor :antes, :despues

    def initialize(antes, despues)
      @antes = antes
      @despues = despues
    end
  end
end