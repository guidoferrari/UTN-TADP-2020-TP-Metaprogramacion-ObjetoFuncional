require_relative '../contratos'

class Guerrero
  include Contratos

  attr_accessor :fuerza, :vida

  #def self.invariant
    #raise "Invariante incumplido" if !yield
  #end

  invariant { vida >= 0 }
  #invariant { fuerza > 0 && fuerza < 100 }

  def initialize(vida, fuerza)
    @fuerza = fuerza
    @vida = vida
  end

  def atacar(otro)
    puts "Fuerza:"
    puts @fuerza
    puts "Vida defensor:"
    puts otro.vida
    otro.vida = otro.vida - @fuerza
  end

end

