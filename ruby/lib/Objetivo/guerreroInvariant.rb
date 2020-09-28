require_relative '../contratos'

class Guerrero
  include Contratos

  attr_accessor :vida, :fuerza

  #def self.invariant
    #raise "Invariante incumplido" if !yield
  #end

  invariant { puts vida }
  #invariant { vida >= 0 }
  #invariant { fuerza > 0 && fuerza < 100 }

  def atacar(otro)
    puts "Fuerza:"
    puts fuerza
    puts "Vida defensor:"
    puts otro.vida
    otro.vida -= fuerza
  end

end

