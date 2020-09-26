require_relative '../contratos'

class Guerrero
  include Contratos

  attr_accessor :vida, :fuerza

  #invariant { vida >= 0 }
  #invariant { fuerza > 0 && fuerza < 100 }


  def atacar(otro)
    otro.vida -= fuerza
  end

end

