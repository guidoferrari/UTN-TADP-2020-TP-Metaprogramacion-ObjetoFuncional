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
      @__contrxxx__ = []
    end
  end

  module ClassMethods
    def method_added(method_name)
      __no_recursivo__ do
        puts "Added #{method_name} method."
        metodo_viejo = self.instance_method(method_name)
        # klass = self
        self.define_method(method_name) do |*args, &block|
          resultado = "esta es una "
          resultado << metodo_viejo.bind(self).call(*args)
          resultado << " de redefinición de métodos"
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
end

