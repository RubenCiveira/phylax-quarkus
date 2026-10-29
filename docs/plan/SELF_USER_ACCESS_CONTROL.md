Las entidades:
 invitaciones
 códigos mfa
así como otras entidades secundarias de la tabla user, si se exponene via fachada, deberían responder a un control de acceso basado en el 

<allow for="all" name="is-self-user" claim-uuid="$self.user" />

El generador debería saber que el atributo "claim-" hace referencia a la existencia de un claim, y $self hace referencia a un "auto objeto".

<fixed-field 
	name="fixed-self-user" field="user" />
<formula 
	on="create,update" name="inherit-user"
    field="user" claim="uid" />
<facade>
    <visibility name="self-user" 
				property="user" claim="uid" />

en todos los casos como la property "user" se descubre como referencia a otra entidad => el generador extrae el uid para la consulta a la hora de pintar / filtrar, etc....