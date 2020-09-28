class Pj
  def initialize
    @vida = 100
  end
  def perder
    @vida -= 10
    if @vida < 0
      raise 'se quedo sin vida'
    end
  end
  def vida
    @vida
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


puts "**************************************"
puts "* Buenos dias Rey juga sin problemas *"
puts "**************************************"

mensaje1= proc {puts 'antes'}
mensaje2= proc {puts 'despues'}
pepe= Pj.new
prueba=Prueba.new(pepe,mensaje1,mensaje2)
prueba.antes_despues!(:perder)
puts pepe.vida
#proc {11.times {pepe.perder}}.call
prueba.lista_mensajes
#en la consola poner require_relative 'PrimerosObjetos' para poder interactuar con esto


