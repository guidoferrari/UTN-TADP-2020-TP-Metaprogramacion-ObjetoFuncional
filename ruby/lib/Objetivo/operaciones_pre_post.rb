require_relative '../contratos'

class Operaciones
  include Contratos

  pre{ divisor != 0 }
  post{ |result| result * divisor == dividendo }
  def dividir(dividendo, divisor)
    resultado = dividendo / divisor
  end

  pre
  def restar(minuendo, sustraendo)
    minuendo - sustraendo
  end

  pre{puts "Esto no es una aserción"}
  post{|resultado| resultado == unSumando - (otroSumando * -1)}
  post{nil}
  def sumar(unSumando, otroSumando)
    unSumando + otroSumando
  end
end