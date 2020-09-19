class MiClase
  attr_reader :before, :after

  #method_added

  #before_and_after_each_call(# Bloque Before. Se ejecuta antes de cada mensaje
  #    proc{ @before = 1 },
      # Bloque After. Se ejecuta después de cada mensaje
  #    proc{ @after = “Salí de un mensaje” }
  #)

  def mensaje_1
    puts “mensaje_1”
    return 5
  end

  def mensaje_2
    puts “mensaje_2”
    return 3
  end

end


