require 'sourcify'

class EjecutadorDeCondiciones

  attr_accessor :receptor

  def ejecutar_condicion(metodo_bindeado, *args, tipo_condition, condicion)
    contexto = generar_contexto(args, metodo_bindeado)
    # raise 'Failed to meet '+ tipo_condition unless (condicion.bloque.call_with_vars(parametrosHash))
  end

  private

  def obtener_bindings(args, metodo_bindeado)

    # TODO DEBERIA GENERAR EL CONTEXTO NO SOLO CON PARAMETROS DEL METODO
    # SI NO TAMBIEN CON METODOS DE INSTANCIA
    parametros = metodoBindeado.unbind.parameters.map(&:last).zip(args).to_h
  end

  def generar_contexto(args, metodo_bindeado)

    # TODO DEBERIA GENERAR EL CONTEXTO NO SOLO CON PARAMETROS DEL METODO
    # SI NO TAMBIEN CON METODOS DE INSTANCIA
    bindings = obtener_bindings(args, metodo_bindeado)
    context = metodo_bindeado.receiver.clone

    #TODO: continuar...
  end
end

#TODO: ver si realmente deberian usar un Struct o un clon del objeto receptor...
class Proc
  def call_with_vars(vars, *args)
    Struct.new(*vars.keys).new(*vars.values).instance_exec(*args, &self)
  end
end