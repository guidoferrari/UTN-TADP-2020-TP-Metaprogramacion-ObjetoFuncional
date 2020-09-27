require_relative('../PrePost.rb')

class Operaciones

  ################################################################

  #precondición de dividir
  pre{ divisor != 0 }
  #postcondición de dividir
  #post{ |result| result * divisor == dividendo }
  def dividir(dividendo, divisor)
    dividendo / divisor
  end

  # este método no se ve afectado por ninguna pre/post condición
  def restar(minuendo, sustraendo)
    minuendo - sustraendo
  end
end

#> Operaciones.new.dividir(4, 0)
# RuntimeError: Failed to meet preconditions
