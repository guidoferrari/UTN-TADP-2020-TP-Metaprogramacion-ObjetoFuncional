require 'sourcify'

class Ejecutador

  def initialize(metodo, instancia, precondiciones, postcondiciones, ejecutarAntes, ejecutarDespues, invariantes, accessors, *args)
    @metodo = metodo
    @instancia = instancia
    @args = *args
    @precondiciones = precondiciones
    @postcondiciones = postcondiciones
    @ejecutarAntes = ejecutarAntes
    @ejecutarDespues = ejecutarDespues
    @invariantes = invariantes
    @accessors = accessors
  end

  def ejecutar_metodo
    @resultado = @metodo.bind(@instancia).call(*@args)
  end

  def ejecutar_antes
    ejecutar(&@ejecutarAntes)
  end

  def ejecutar_despues
    ejecutar(&@ejecutarDespues)
  end

  def ejecutar_precondiciones
    ejecutar_condiciones('precondition', @precondiciones)
  end

  def ejecutar_postcondiciones
    ejecutar_condiciones('postcondition', @postcondiciones)
  end

  def ejecutar_invariantes
    @invariantes.each {|invariante| ejecutar_invariante(invariante)} unless @accessors.include? @metodo.name.to_sym
  end

  private
  def generar_contexto(args, metodo_bindeado)

    bindings = metodo_bindeado.unbind.parameters.map(&:last).zip(args).to_h
    context = metodo_bindeado.receiver.clone

    bindings.each { |nombre, valor| context.singleton_class.send(:define_method, nombre) {valor} }
    context
  end

  def ejecutar(&bloque)
    @instancia.instance_exec &bloque if bloque
  end

  def ejecutar_condiciones(tipo_condition, condiciones)
    contexto = generar_contexto(@args, @metodo.bind(@instancia))
    condiciones.each {|condicion| ejecutar_condicion(contexto, tipo_condition, condicion)}
  end

  def ejecutar_condicion(contexto, tipo_condicion, condicion)
    raise 'Failed to meet '+ tipo_condicion if (contexto.instance_exec @resultado, &condicion) == false
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