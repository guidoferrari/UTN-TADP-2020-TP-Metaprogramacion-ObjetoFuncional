class Prueba

  def materia
    :tadp
  end
end
puts "buenas"

module Invariante
  #attr_accessor :vida
  def invariant
    raise 'no paso el invariant'
  end
end
class Object
  include Invariante
end

class Pj
  attr_accessor :vida
  #invariant
  def initialize (vida)
    @vida = vida
  end

  def perder
    @vida -= 10
  end

end


class Prueba
  def initialize(objeto_prueba,antes,despues)
    @mensajes = []
    @objeto = objeto_prueba
    @antes = antes
    @despues = despues
    @mensaje_actual = nil
  end

  def  before_and_after_each_call(bloque1,bloque2)
    bloque1.call
    @objeto.send (@mensaje_actual)
    bloque2.call
  end

  def antes_despues!(mensaje)
    @mensajes.push mensaje
    @mensaje_actual = mensaje
    self.before_and_after_each_call(@antes,@despues)
  end

  def lista_mensajes
    puts @mensajes
  end

  def antes
    puts 'antes'
  end

  def despues
    puts 'despues'
  end
end


mensaje1= proc {puts 'antes'}
mensaje2= proc {puts 'despues'}
pepe= Pj.new(100)
prueba=Prueba.new(pepe,mensaje1,mensaje2)
prueba.antes_despues!(:perder)
puts pepe.vida
#proc {11.times {pepe.perder}}.call
prueba.lista_mensajes
#en la consola poner require_relative 'PrimerosObjetos' para poder interactuar con esto



