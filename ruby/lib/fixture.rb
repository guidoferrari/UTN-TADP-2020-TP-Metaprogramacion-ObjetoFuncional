require_relative 'contratos'

class Prueba
  include Contratos

  def materia
    puts "método materia"
    "prueba"
  end
end
