require_relative 'contratos'

class Prueba
  include Contratos

  before_and_after_each_call(
      # Bloque Before. Se ejecuta antes de cada mensaje
      proc{ puts "Ejecuté esto antes" },
      # Bloque After. Se ejecuta después de cada mensaje
      proc{ puts "Ejecuté esto después" }
  )

  def metodoAEjecutar
    puts "ejecutando"
  end
end

