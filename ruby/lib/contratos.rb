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

    attr_accessor :__invariantes__

    def before_and_after_each_call(procAntes, procDespues)
      @__antes_despues__ = AntesDespues.new(procAntes, procDespues)
    end

    def invariant(&block)
      @__invariantes__ << Invariante.new(&block)
    end

    def pre (&block)
      #puts 'antes de asignar'
      #@__pre = block
      # @__pre = Precondicion.initialize(block)
      #puts 'despues de asignar'
    end

    def post (&block)
      @__post = block
    end

    def method_added(method_name)
      __no_recursivo__ do
        puts "Added #{method_name} method."
        metodo_viejo = self.instance_method(method_name)
        if(@__antes_despues__)
          ejecutarAntes = @__antes_despues__.antes
          ejecutarDespues = @__antes_despues__.despues
        end

        # if @__pre != nil
        #   precondition = @__pre
        #   @__pre = nil
        # end
        #
        # if @__post != nil
        #   postcondition = @__post
        #   @__post = nil
        # end

        klass = self
        #puts klass.inspect

        puts 'invariantes = ' + @__invariantes__.inspect

        self.define_method(method_name) do |*args, &block|
          #puts "define_method"
          #puts method_name
          #puts args
          puts 'self dentro de define = ' + self.inspect
          puts 'invariantes dentro de define = ' + self.class.__invariantes__.inspect

          puts 'self = ' + self.inspect

          #
          # if precondition != nil
          #   ejecutar_precondicion = proc do |*args, &block|
          #     if (block.call(*args)) == false
          #       raise RuntimeError('Failed to meet preconditions')
          #     end
          #   end
          # end

          # if precondition != nil
          #   def metodo_viejo(*args, &block)
          #     if proc{|*args| precondition}.call == false
          #       raise new RuntimeError('Failed to meet preconditions')
          #     end
          #     yield
          #   end
          # end

          #klass.verificarPrecondicion.bind(self).call(*args, ejecutar_precondicion) if precondition != nil
          #ejecutar_precondicion.call(*args, &precondition) if precondition != nil
          #raise new RuntimeError('Failed to meet preconditons') if @__pre != nil && precondition.call(*args) == false
          # if precondition != nil
          #   block = ejecutar_precondicion >> block
          # end

          # if precondition != nil
          #   block = precondition >> block
          #   precondition = nil
          # end
          #self.instance_exec(*args, precondition.validar)

          ejecutarAntes.call() if ejecutarAntes
          resultado = metodo_viejo.bind(self).call(*args, &block)
          ejecutarDespues.call() if ejecutarDespues

          self_guardado = self
          self.class.__invariantes__.each{|invariante| self_guardado.instance_exec(*args, &invariante.validar)}

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

    def initialize(&bloque)
      @bloque = bloque
    end

    def validar
      #raise "Invariante incumplido" if !proc(&@bloque).call
      Proc.new do
        puts 'estamos dentro del invariante ' + self.inspect
        puts @bloque.inspect
        raise "Invariante incumplido" if !(@bloque.call)
      end
    end
  end

  # class Precondicion
  #   attr_accessor :bloque
  #
  #   def initialize(&bloque)
  #     @bloque = bloque
  #   end
  #
  #   def validar
  #     proc {raise 'Failed to meet precondition' if !bloque.call == false}
  #   end
  # end
end

