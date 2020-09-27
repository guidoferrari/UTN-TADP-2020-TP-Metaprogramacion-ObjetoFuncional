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
    end
  end

  module ClassMethods

    def before_and_after_each_call(procAntes, procDespues)
      @__antes_despues__ = AntesDespues.new(procAntes, procDespues)
    end

    #def invariant
    #  @__invariantes__ << Invariante.new {yield}
    #end

    def method_added(method_name)
      __no_recursivo__ do
        puts "Added #{method_name} method."
        metodo_viejo = self.instance_method(method_name)
        if(@__antes_despues__)
          ejecutarAntes = @__antes_despues__.antes
          ejecutarDespues = @__antes_despues__.despues
        end
        # klass = self
        self.define_method(method_name) do |*args, &block|
          #puts "define_method"
          #puts method_name
          #puts args
          ejecutarAntes.call() if ejecutarAntes
          resultado = metodo_viejo.bind(self).call(*args)
          ejecutarDespues.call() if ejecutarDespues
          # TODO Recorrer y validar @__invariantes__
          resultado
        end
      end
    end

    def __no_recursivo__
      return if Thread.current[:__ejecutando__]

      Thread.current[:__ejecutando__] = true
      yield if block_given?
      Thread.current[:__ejecutando__] = false
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

    def initialize(&bloque)
      @bloque = bloque
    end

    def validar
      raise "Invariante incumplido" if !proc(&@bloque).call
    end
  end
end

