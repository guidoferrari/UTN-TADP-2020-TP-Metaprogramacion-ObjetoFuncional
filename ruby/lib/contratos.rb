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

        accessors, ejecutar_antes, ejecutar_despues, invariantes, metodo_viejo, postcondiciones, precondiciones = guardar_variables_instancia(method_name)

        self.define_method(method_name) do |*args|

          #TODO: Crear ejecutador al principio e ir agregando las cosas a medida que llegan
          ejecutador = Ejecutador.new(metodo_viejo, self, precondiciones, postcondiciones, ejecutar_antes, ejecutar_despues, invariantes, accessors, *args)

          ejecutador.ejecutar_precondiciones
          ejecutador.ejecutar_antes
          resultado = ejecutador.ejecutar_metodo
          ejecutador.ejecutar_despues
          ejecutador.ejecutar_invariantes
          ejecutador.ejecutar_postcondiciones
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

    def before_and_after_each_call(proc_antes, proc_despues)
      @__antes_despues__ = AntesDespues.new(proc_antes, proc_despues)
    end

    def invariant(&bloque)
      @__invariantes__ << Proc.new(&bloque) if block_given?
    end

    def pre(&bloque)
      @__precondiciones__ << Proc.new(&bloque) if block_given?
    end

    def post(&bloque)
      @__postcondiciones__ << Proc.new(&bloque) if block_given?
    end

    def attr_accessor(*args)
      @__accessors__ += args
      super
    end

    def attr_reader(*args)
      @__accessors__ += args
      super
    end

    private

    def guardar_variables_instancia(method_name)
      metodo_viejo = self.instance_method(method_name)
      ejecutar_antes = @__antes_despues__.antes unless @__antes_despues__.nil?
      ejecutar_despues = @__antes_despues__.despues unless @__antes_despues__.nil?
      invariantes = @__invariantes__
      accessors = @__accessors__
      precondiciones = @__precondiciones__
      postcondiciones = @__postcondiciones__
      @__precondiciones__ = []
      @__postcondiciones__ = []

      return accessors, ejecutar_antes, ejecutar_despues, invariantes, metodo_viejo, postcondiciones, precondiciones
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