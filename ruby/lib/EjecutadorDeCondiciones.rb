require 'sourcify'

class EjecutadorDeCondiciones

  def ejecutar_condicion(metodo_bindeado, *args, tipo_condition, condicion, resultado)
    contexto = generar_contexto(args, metodo_bindeado)

    raise 'Failed to meet '+ tipo_condition unless (contexto.instance_exec resultado, &condicion.bloque)
  end

  private

  def obtener_bindings(args, metodo_bindeado)
    metodo_bindeado.unbind.parameters.map(&:last).zip(args).to_h
  end

  def generar_contexto(args, metodo_bindeado)

    bindings = obtener_bindings(args, metodo_bindeado)
    context = metodo_bindeado.receiver.clone

    bindings.each { |nombre, valor| context.singleton_class.send(:define_method, nombre) {valor} }
    context
  end
end