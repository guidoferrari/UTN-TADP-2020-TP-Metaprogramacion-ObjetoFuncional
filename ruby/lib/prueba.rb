class Sarasa

  @controlados = []

  def self.method_added(method_name)
    puts "method_added"
    if @controlados.instance_of? NilClass
      @controlados = Array.new
    end

    #metodos = self.instance_methods false
    if method_name.equal? :materia and !@controlados.include? method_name
      @controlados << method_name

      #self.define_method(method_name) do |*args, &fn|
      self.send(:define_method, method_name) {
        puts "define_method"

        #nombreAnterior = method_name
        #original_result = send(method_name, *args, &fn)
        #metodo = method(method_name)
        #puts self

        if !@llamadaenproceso

          metodo = self.class.instance_method(method_name)
          #metodo = self.method(method_name)

          @llamadaenproceso = true
          resultado = "esta es una "
          #resultado << metodo.bind_call(self.class)
          #resultado << metodo.bind(self).call(*args)
          #resultado << metodo.bind(self).call
          #resultado << metodo.call
          #resultado << instance_eval(metodo)
          resultado << send(metodo)
          resultado << " de redefinición de métodos"

          @llamadaenproceso = false
        end
        return resultado
      }
        #end
    end
  end
end

class Prueba < Sarasa

  def materia
    puts "método materia"
    "prueba"
  end
end