require_relative '../Contratos'

class MiClase
  include Contratos

  before_and_after_each_call(
      # Bloque Before. Se ejecuta antes de cada mensaje
      proc{ puts "Entré a un mensaje" },
      # Bloque After. Se ejecuta después de cada mensaje
      proc{ puts "Salí de un mensaje" }
  )

  def mensaje_1
    puts "mensaje_1"
    return 5
  end

  def mensaje_2
    puts "mensaje_2"
    return 3
  end

end
