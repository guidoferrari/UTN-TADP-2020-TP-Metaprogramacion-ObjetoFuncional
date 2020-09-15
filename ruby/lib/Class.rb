class Class
  @pre
  @post

  def anotacion_de_metodo(anotacion, valor)
    @annotation_list[anotacion] = valor
  end

  def self.define_method(nombreMetodo)
    super

    if @pre != nil && @post == nil
      metodo = instance_method(nombreMetodo)

      define_method(nombreMetodo) do |*args, &block|
        unless @pre
          raise RuntimeError
        end
        metodo.bind(self).(*args, &block)
      end
    end
  end

  def post
    @post = block
  end

  def pre
    @pre = block
  end

end