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

  def ejecutar_condiciones(tipo_condition, condiciones, resultado)
    contexto = generar_contexto(@args, @metodo.bind(@instancia))
    condiciones.each {|condicion| ejecutar_condicion(contexto, tipo_condition, condicion, resultado)}
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

  def ejecutar_condicion(contexto, tipo_condicion, condicion, resultado)
    raise 'Failed to meet '+ tipo_condicion unless (contexto.instance_exec resultado, &condicion)
  end

  def ejecutar_invariante(invariante)
    raise 'Invariant failed: '+ block_to_s(&invariante) unless (evaluar_invariante(invariante))
  end

  def evaluar_invariante(invariante)
    begin
      @instancia.instance_exec &invariante
    rescue NoMethodError => e
      true
    end
  end

  def block_to_s(&blk)
    blk.to_source(:strip_enclosure => true)
  end
end