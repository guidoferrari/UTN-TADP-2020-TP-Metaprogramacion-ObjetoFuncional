require_relative '../contratos'

class Guerrero
  include Contratos

  attr_accessor :vida, :fuerza

  def initialize(vida=0, fuerza=0)
    self.vida = vida
    self.fuerza = fuerza
  end

  invariant { vida >= 0 }
  invariant { fuerza > 0 && fuerza < 100 }

  def atacar(otro)
    otro.vida -= fuerza
  end

end

