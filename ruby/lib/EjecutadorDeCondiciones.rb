require 'sourcify'
class EjecutadorDeCondiciones

  def self.ejecutar_condicion(metodoBindeado, *args, tipoDeCondicion, condicion)
    parametrosHash = generarContexto(args, metodoBindeado)
    raise 'Failed to meet '+ tipoDeCondicion unless (condicion.bloque.call_with_vars(parametrosHash))
  end

  private

  def self.generarContexto(args, metodoBindeado)

    # TODO DEBERIA GENERAR EL CONTEXTO NO SOLO CON PARAMETROS DEL METODO
    # SI NO TAMBIEN CON METODOS DE INSTANCIA

    parametros = metodoBindeado.send(:parameters).map { |x| x[1] }

    i = -1
    parametrosHash = Hash[parametros.map { |key| [key, args[i += 1]] }]
  end
end

class Proc
  def call_with_vars(vars, *args)
    Struct.new(*vars.keys).new(*vars.values).instance_exec(*args, &self)
  end
end