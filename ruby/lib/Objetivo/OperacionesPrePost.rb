require_relative '../contratos'

class Operaciones
  include Contratos

  pre{ divisor != 0 }
  post{ |result| result * divisor == dividendo }
  def dividir(dividendo, divisor)
    resultado = dividendo / divisor
  end

  def restar(minuendo, sustraendo)
    minuendo - sustraendo
  end
end