#require 'sourcify'
class EjecutadorDeCondiciones

  def self.ejecutar_condicion(metodoBindeado, *args, tipoDeCondicion, condicion)
    #raise 'Failed to meet '+ tipoDeCondicion unless (contexto.instance_exec &condicion.bloque)
    #Por ahora le estoy errando al contexto
    parametros = metodoBindeado.send(:parameters).map { |x| x[1]}
    i = -1
    parametrosHash = Hash[parametros.map{|key| [key, args[i += 1]]}]
    raise 'Failed to meet '+ tipoDeCondicion unless (lambda{condicion}.call_with_vars(parametrosHash))
  end
end

class Proc
  def call_with_vars(vars, *args)
    # aca estoy creando un contexto con los valores del método pero no logro que se ejecute bien
    Struct.new(*vars.keys).new(*vars.values).instance_exec(*args, &self)
  end


end