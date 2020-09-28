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

    def invariant(&bloque)
      #@__invariantes__ << Invariante.new {yield}
      #@__invariantes__ << Invariante.new(&bloque)
      @__invariantes__ << AntesDespues.new(proc(&bloque), proc(&bloque))
    end


    def method_added(method_name)
      __no_recursivo__ do
        puts "Added #{method_name} method."
        metodo_viejo = self.instance_method(method_name)
        if(@__antes_despues__)
          ejecutarAntes = @__antes_despues__.antes
          ejecutarDespues = @__antes_despues__.despues
        end
        # klass = self
        invariantes = @__invariantes__
        self.define_method(method_name) do |*args, &block|
          #puts "define_method"
          #puts method_name
          #puts args

          invariantes.each { |invariante| invariante.antes.call }

          ejecutarAntes.call() if ejecutarAntes
          resultado = metodo_viejo.bind(self).call(*args)
          ejecutarDespues.call() if ejecutarDespues

          # TODO Recorrer y validar @__invariantes__
          #target = self
          #self.class.__invariantes__.each{|invariante| target.instance_exec(*args, &invariante.validar)}
          #invariantes.each{|invariante| invariante.validar(target)}
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

=begin
  class Invariante
    attr_accessor :bloque

    def initialize(&bloque)
      @bloque = bloque
    end

    def validar(target)
      puts "validar"
      puts @bloque
      #proc(&@bloque).call
      target.instance_eval(&@bloque)

      #raise "Invariante incumplido" if !proc(&@bloque).call
      #raise "Invariante incumplido" if !target.instance_eval(&@bloque)
      #raise "Invariante incumplido" if !target.instance_exec(*args, &@bloque)

    end
  end
=end
end

