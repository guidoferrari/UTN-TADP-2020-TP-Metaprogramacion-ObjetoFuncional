require_relative '../contratos'

class Persona
  include Contratos

  attr_accessor :nombre, :apellido
  attr_reader :lugarDeNacimiento

  invariant { !lugarDeNacimiento.nil? }

  def initialize(nombre, apellido, lugarDeNacimiento)
    @marca = nombre
    @porcentaje = apellido
    @lugarDeNacimiento = lugarDeNacimiento
  end
end

