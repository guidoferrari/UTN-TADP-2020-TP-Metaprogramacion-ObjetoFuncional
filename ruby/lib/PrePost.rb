class Module
  attr_accessor :prec, :postc

  alias_method :__define_method, :define_method

  def self.define_method(selector, *args, &block)

    __define_method(selector, *args, &block) unless (!prec.nil? || !post.nil?)

    metodo = instance_method(selector)

    __define_method(selector) do |*args, &block|
      raise RuntimeError('Failed to meet preconditions') unless @pre || @pre.nil?
      metodo.bind(self).(*args, &block)
      raise RuntimeError('Failed to meet postconditions') unless @post || @post.nil?
    end
  end

  def post(&block)
    @postc = block
  end

  def pre(&block)
    @prec = block
  end
end