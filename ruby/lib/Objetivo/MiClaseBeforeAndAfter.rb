class MiClase

  attr_reader :before, :after


  def mensaje_1
    puts "mensaje_1"
    return 5
  end

  def mensaje_2
    puts "mensaje_2"
    return 3
  end

end

mi_clase = MiClase.new
mi_clase.singleton_class.include(before)
mi_clase.singleton_class.include(after)
def mi_clase.singleton_class
  def mensaje_1

  end
end


module before_and-after
  def before
    puts "esto se ejecuta antes"
    super
  end
  def after
    puts "Esto se ejecuta despues"
  end
def mensaje_1
  self.before
  super
  self.after
end

end

before = Proc {puts "esto se ejecuta antes"}
after = Proc {puts "Esto se ejecuta despues"}




