class BasicObject
  @prec
  @postc

  def anotacion_de_metodo(anotacion, valor)
    @annotation_list[anotacion] = valor
  end

=begin
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
=end

  def post
    @postc = block
  end

  def self.pre
    #@prec = block
    #puts block
    puts 'asd'
  end

end