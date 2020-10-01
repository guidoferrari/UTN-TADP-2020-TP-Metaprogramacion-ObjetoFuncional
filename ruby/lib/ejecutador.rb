require 'sourcify'

class Ejecutador

  def initialize(metodo, instancia, *args)
    @metodo = metodo
    @instancia = instancia
    @args = *args
  end

  def ejecutarMetodo
    @metodo.bind(@instancia).call(*@args)
  end

  def ejecutar(&bloque)
    @instancia.instance_exec &bloque
  end

  def ejecutar_condicion(tipo_condition, condicion, resultado)
    contexto = generar_contexto(@args, @metodo.bind(@instancia))
    raise 'Failed to meet '+ tipo_condition unless (contexto.instance_exec resultado, &condicion.bloque)
  end

  def ejecutar_invariantes(invariantes)
    invariantes.each {|invariante| ejecutar_invariante(invariante)}
  end

  private
  def generar_contexto(args, metodo_bindeado)

    bindings = metodo_bindeado.unbind.parameters.map(&:last).zip(args).to_h
    context = metodo_bindeado.receiver.clone

    bindings.each { |nombre, valor| context.singleton_class.send(:define_method, nombre) {valor} }
    context
  end

  def ejecutar_invariante(invariante)
    raise 'Invariant failed: '+ block_to_s(&invariante.bloque) unless (evaluar_invariante(invariante))
  end

  def evaluar_invariante(invariante)
    begin
      @instancia.instance_exec &invariante.bloque
    rescue NoMethodError => e
      true
    end
  end

  def block_to_s(&blk)
    blk.to_source(:strip_enclosure => true)
  end
end