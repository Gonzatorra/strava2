package com.strava.servicios;

public class GestionEstadoSingleton {
    private static GestionEstadoSingleton instancia;
    private boolean usuarioAutenticado;

    private GestionEstadoSingleton() {
        this.setUsuarioAutenticado(false);
    }

    public static GestionEstadoSingleton getInstancia() {
        if (instancia == null) {
            instancia = new GestionEstadoSingleton();
        }
        return instancia;
    }

	public boolean isUsuarioAutenticado() {
		return usuarioAutenticado;
	}

	public void setUsuarioAutenticado(boolean usuarioAutenticado) {
		this.usuarioAutenticado = usuarioAutenticado;
	}
}