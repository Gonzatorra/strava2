package com.strava.fachada;

import com.google.server.GoogleAuthClient;
import com.google.server.Usuario;
import com.strava.DTO.*;
import com.meta.*;
import com.strava.config.AppConfig;
import com.strava.servicios.*;
import com.google.server.UsuarioRepository;
import com.meta.RemoteAuthFacadeMeta;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

public class RemoteFacade extends UnicastRemoteObject implements IRemoteFacade {
    public RemoteAuthFacadeMeta remoteAuthFacadeMeta;//objetos de proyecto meta
    private EntrenamientoService entrenamientoService;
    private RetoService retoService;
    private ServicioAutentificacion servicioAutentificacion;
    private static HashMap<String, String> tokensActivos = new HashMap<>();
    private final GoogleAuthClient googleAuthClient;
    private UsuarioService usuarioService;

//////////////////////////////////////////////////////////////////////////////////////////////////////////
    //error solucionado
    @Override
    public List<String> getUsersActivos() throws RemoteException {
        return new ArrayList<>(tokensActivos.keySet());
    }


	public static void setTokensActivos(HashMap<String, String> tokensActivos) {
		RemoteFacade.tokensActivos = tokensActivos;
	}

	public RemoteFacade(ApplicationContext context) throws RemoteException {
        super();
        //this.remoteAuthFacadeMeta=???? //da null en linea 217 aprox
        this.googleAuthClient = context.getBean(GoogleAuthClient.class); // Obtener el GoogleAuthClient desde el contexto de Spring
        this.usuarioService = new UsuarioService();
        this.entrenamientoService = new EntrenamientoService();
        this.retoService = new RetoService();
        this.servicioAutentificacion = new ServicioAutentificacion();
        

    }

    @Override
    public ArrayList<Integer> getAmigos(UsuarioDTO usuario) throws RemoteException {
        // TODO Auto-generated method stub
        return (ArrayList<Integer>) usuarioService.getAmigos(usuario);
    }


    @Override
    public UsuarioService getUsuarioService() throws RemoteException {
        // TODO Auto-generated method stub
        return this.usuarioService;
    }



    @Override
    public HashMap<Integer, UsuarioDTO> getUsuarios() throws RemoteException {
        // TODO Auto-generated method stub
        return this.usuarioService.getUsuarios();
    }




    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }




    @Override
    public UsuarioDTO registrarUsuario(String username, String password, String email, String nombre, String proveedor) throws RemoteException {
        UsuarioDTO usuario = usuarioService.getUsuarios().values().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);

        if (usuario != null) {
            System.out.println("Usuario ya existe: " + username);
            return usuario;
        }

        return usuarioService.registrar(username, password, email, nombre, proveedor);
    }

    @Override
    public UsuarioDTO login(String username, String contrasena) throws RemoteException {
    	/*
        for (UsuarioDTO u: usuarioService.getUsuarios().values()) {
            if (u.getUsername().equals(username) && u.getProveedor().equals("Strava")) {
                System.out.println(u.getUsername()+u.getProveedor());
                if(tokensActivos.get(username)!=null) {
                    return u;

                }
                break;
            }
        }
        UsuarioDTO usu= usuarioService.obtenerUsuarioPorNombre(username);
        if(usu.getProveedor().equals("Strava")) {
            UsuarioDTO u= usuarioService.login(username, contrasena);
            String token= servicioAutentificacion.autenticar(username, contrasena, "Strava", u.getProveedor());
            if (token!=null) {
	            u.setToken(token);
	            usuarioService.actualizarUsuario(u);
	            UsuarioDTO usu2= UsuarioService.getUsuarios().get(u.getId());
	            tokensActivos.put(usu2.getUsername(), usu2.getToken());
	            return usu;
            }
            else {
            	return null;
            }
        }
        return null;
		*/
    	
    	System.out.println("Usuario que quiere iniciar sesión: " + username);
        
        if (tokensActivos.containsKey(username)) {
            System.out.println("Usuario ya loggead.");
            return null;//usuarioService.obtenerUsuarioPorNombre(username);
        }

        UsuarioDTO user = usuarioService.obtenerUsuarioPorNombre(username);
        if (user == null) {
            System.err.println("No encontrado: " + username);
            return null;
        }

        if (!user.getContrasena().equals(contrasena)) {
            System.err.println("Credenciales invalidas: " + username);
            return null;
        }

        String token = servicioAutentificacion.autenticar(username, contrasena, "Strava", user.getProveedor());
        if (token == null) {
            System.err.println("Autenticación fallida: " + username);
            return null;
        }

        user.setToken(token);
        usuarioService.actualizarUsuario(user);
        tokensActivos.put(username, token);
        UsuarioService.getUsuarios().put(user.getId(), user); // Ensure user data is consistent
        System.out.println("Login exitoso para: " + username);
        return user;

    }

    @Override
    public UsuarioDTO loginConProveedor(String username, String password, String plataforma) throws RemoteException {
        String token = null;
        UsuarioDTO usuario = usuarioService.obtenerUsuarioPorNombre(username);

        /*if (usuario != null) {
	        String proveedor = usuario.getProveedor();
	
	        //Verificar si la plataforma del usuario coincide con la proporcionada
	        if (plataforma.equalsIgnoreCase(proveedor)) {
	            //Verificacion para Google
	            if ("Google".equalsIgnoreCase(plataforma)) {
	                if(usuario.getToken()!=null) {
		                if (tokensActivos.containsKey(token)) {
		                    System.out.println("Login realizado correctamente en Google.");
		                    return usuario;
		                } 
	            	}
	            	else {
	                    System.err.println("Token no encontrado");
	                    token = googleAuthClient.loginUser(username, password);
	                    usuario.setToken(token); //Establecer el token en el usuario
	                    usuarioService.actualizarUsuario(usuario); //Actualizar el usuario en el servicio
		                UsuarioDTO usuarioActualizado = usuarioService.getUsuarios().get(usuario.getId()); //Obtener el usuario actualizado
		                return usuarioActualizado; //Retornar el usuario actualizado
	                    
	                }
	
	            }
	            //Verificacion para Meta
	            else if ("Meta".equalsIgnoreCase(plataforma)) {
	                AuthClientMeta metaAuthClient = new AuthClientMeta("localhost", 1101);
	                if(usuario.getToken()!=null) {
		                if (tokensActivos.containsKey(token)) {
		                    System.out.println("Login realizado correctamente en Meta.");
		                    return usuario;
		                }
	                 }
	                 else {
	                    System.err.println("Token no encontrado");
	                    try {
							token = metaAuthClient.login(username, password);
							usuario.setToken(token); //Establecer el token en el usuario
			                usuarioService.actualizarUsuario(usuario); //Actualizar el usuario en el servicio
			                UsuarioDTO usuarioActualizado = usuarioService.getUsuarios().get(usuario.getId()); //Obtener el usuario actualizado
			                return usuarioActualizado; //Retornar el usuario actualizado
						} catch (IOException e) {
							// TODO Auto-generated catch block
							System.err.println("Error al asignar token desde "+ plataforma);
							e.printStackTrace();
						}
	                    
	                 }
	                
	            }
	
	         
	            
	        } else {
	            //Si la plataforma no coincide con el proveedor del usuario, indicar login fallido
	            System.out.println("Login fallido con plataforma: " + plataforma + " para usuario con proveedor: " + proveedor);
	            return null;
	        }
        }*/
        //else {
        	//mirar si existe ese usuario en proveedor
        	//si existe, registrar en strava, asignar token
        	if (plataforma.equalsIgnoreCase("Google")) {
        	    // Use JPA to see if that user is in the DB
        		List <Usuario> usuariosG = googleAuthClient.allUsers();
        		boolean encontrado= false;
        		int i=0;
        		while (!encontrado & i< usuariosG.size()) {
        			if(usuariosG.get(i).getUsername().equalsIgnoreCase(username)) {
        				encontrado=true;
        			}
        			i++;
        		}
        		if(encontrado) {
        			token = googleAuthClient.loginUser(username, password);
        	        UsuarioDTO usuarioG= usuarioService.registrar(username, password, username + "@google.com", token, "Google");
        	        tokensActivos.put(usuarioG.getUsername(), token);
        	        usuarioService.getUsuarios().put(usuarioG.getId(), usuarioG);
        	        return usuarioG;
        	    }
        	} 
	        else if (plataforma.equalsIgnoreCase("Meta")){
	        	//////////////////////////////////////////////////////////////////////////////////////////////
	        	Map<String, String> userStore = remoteAuthFacadeMeta.getUserStore();
	        	if(userStore.containsKey(username)) {
	        	if(true) {
	        		AuthClientMeta metaAuthClient = new AuthClientMeta("localhost", 1101);
	        		try {
						token = metaAuthClient.login(username, password);
						UsuarioDTO usuarioM= usuarioService.registrar(username, password, username+"@meta.com", token, "Meta");
		        		tokensActivos.put(usuarioM.getUsername(), token);
	        	        usuarioService.getUsuarios().put(usuarioM.getId(), usuarioM);
		        		return usuarioM;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	}
	        }
	    }
        
    	return null;
    }


    /*
    private UsuarioDTO autenticacionGoogle(String username, String password) {
        try {
            String result = externoService.verifyGoogle(username, password);
            if (result != null) {
                UsuarioDTO usuario = new UsuarioDTO();
                usuario.setUsername(username);
                usuario.setContrasena(password);
                usuario.setProveedor("Google");
                usuario.setEntrenamientos(new ArrayList<>());
                usuario.setRetos(new HashMap<>());
                usuario.setAmigos(new ArrayList<>());
                usuarioService.registrarUsuario(usuario);
                return usuario;
            } else {
                System.out.println("Autenticación fallida para el usuario: " + username);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private UsuarioDTO autenticacionMeta(String username, String password) {
        try {
            String result = externoService.verifyMeta(username, password);
            if (result != null) {
                UsuarioDTO usuario = new UsuarioDTO();
                usuario.setUsername(username);
                usuario.setContrasena(password);
                usuario.setProveedor("Meta");
                usuario.setEntrenamientos(new ArrayList<>());
                usuario.setRetos(new HashMap<>());
                usuario.setAmigos(new ArrayList<>());
                usuarioService.registrarUsuario(usuario);
                return usuario;
            } else {
                System.out.println("Autenticación fallida para el usuario: " + username);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /*
    @Override
    public void logout(String token) throws RemoteException {

        usuarioService.logout(token);
    }
	*/

    @Override
    public void logout(String username) throws RemoteException {
    	/*
        UsuarioDTO usuario = usuarioService.obtenerUsuarioPorNombre(username);
        if (usuario != null) {
            String token= usuario.getToken();
            String proveedor = usuario.getProveedor();
            if ("Google".equals(proveedor)) {
            	tokensActivos.remove(usuario.getUsername());
                //googleAuthClient.logoutUser(username);
            } else if ("Meta".equals(proveedor)) {
                try {
                    AuthClientMeta metaAuthClient = new AuthClientMeta("localhost", 1101);
                    metaAuthClient.sendRequest("LOGOUT;" + username);
                    tokensActivos.remove(usuario.getUsername());
                    System.out.println("Logout realizado correctamente en Meta.");
                } catch (IOException e) {
                    System.err.println("Error durante el logout en Meta: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            else {
                usuarioService.logout(token);
                tokensActivos.remove(username);
            }

            System.out.println("Logout completo para el usuario: " + username);
        } else {
            System.out.println("Usuario no encontrado para logout: " + username);
        }
        */
    	UsuarioDTO usuario = usuarioService.obtenerUsuarioPorNombre(username);
        if (usuario != null) {
            String token = usuario.getToken();
            String proveedor = usuario.getProveedor();

            if ("Google".equals(proveedor)) {
                tokensActivos.remove(username);
            } else if ("Meta".equals(proveedor)) {
                try {
                    AuthClientMeta metaAuthClient = new AuthClientMeta("localhost", 1101);
                    metaAuthClient.sendRequest("LOGOUT;" + username);
                    tokensActivos.remove(username);
                    System.out.println("Logout realizado correctamente en Meta.");
                } catch (IOException e) {
                    System.err.println("Error durante el logout en Meta: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                usuarioService.logout(token);
                tokensActivos.remove(username);
            }

            usuario.setToken(null);
            usuarioService.actualizarUsuario(usuario);

            System.out.println("Logout completo para el usuario: " + username);
        } else {
            System.out.println("Usuario no encontrado para logout: " + username);
        }
    }


    @Override
    public void eliminarUsuario(int userId) throws RemoteException {
        UsuarioDTO usu = usuarioService.getUsuarios().get(userId);

        usuarioService.eliminarUsuario(usu);
    }

    @Override
    public void actualizarUsuario(UsuarioDTO usuarioDTO) throws RemoteException {
        usuarioService.actualizarUsuario(usuarioDTO);
    }


    @Override
    public RetoDTO crearReto(String nombre, LocalDateTime fecIni, LocalDateTime fecFin, float objetivoDistancia,
                             float objetivoTiempo, String deporte, UsuarioDTO usuarioCreador, List<UsuarioDTO> participantes)
            throws RemoteException {
        return retoService.crearReto(nombre, fecIni, fecFin, objetivoDistancia, objetivoTiempo, deporte, usuarioCreador, participantes);

    }

    @Override
    public void aceptarReto(UsuarioDTO usuario, RetoDTO reto) throws RemoteException {
        retoService.aceptarReto(usuario, reto);

    }

    @Override
    public HashMap<Integer,RetoDTO> visualizarReto() throws RemoteException {
        return retoService.visualizarReto();

    }


    @Override
    public void eliminarReto(UsuarioDTO usuario, RetoDTO reto) throws RemoteException {
        retoService.eliminarReto(usuario, reto);

    }

    @Override
    public List<Integer> obtenerClasificacion(RetoDTO reto) throws RemoteException {
        return retoService.obtenerClasificacion(reto);
    }

    @Override
    public void calcularProgreso(UsuarioDTO usuario) throws RemoteException {
        retoService.calcularProgreso(usuario);

    }

    @Override
    public void actualizarReto(RetoDTO reto, String nombre, LocalDateTime fechaIni, LocalDateTime fechaFin, float distancia,
                               float tiempo, UsuarioDTO usuarioCreador, String deporte, ArrayList<Integer> participantes) throws RemoteException {
        retoService.actualizarReto(reto, nombre, fechaIni, fechaFin, distancia, tiempo, usuarioCreador, deporte, participantes);
    }

    @Override
    public EntrenamientoDTO crearEntreno(UsuarioDTO usuario, String titulo, String deporte, double distancia, LocalDate fechaIni,
                                         float horaInicio, double duracion) throws RemoteException {
        return entrenamientoService.crearEntreno(usuario, titulo, deporte, distancia, fechaIni, horaInicio, duracion);

    }

    @Override
    public void actualizarEntreno(EntrenamientoDTO entrenamiento, UsuarioDTO usu, String titulo, String deporte, double distancia, double duracion) throws RemoteException {
        entrenamientoService.actualizarEntreno(entrenamiento, usu, titulo, deporte, distancia, duracion);

    }

    @Override
    public void eliminarEntreno(int index, EntrenamientoDTO entrenamiento) throws RemoteException {
        entrenamientoService.eliminarEntreno(index, entrenamiento);

    }

    @Override
    public void visualizarEntreno(EntrenamientoDTO entrenamiento) throws RemoteException {
        entrenamientoService.visualizarEntreno(entrenamiento);

    }


}