package com.strava.fachada;

import com.google.server.GoogleAuthClient;
import com.google.server.Usuario;
import com.strava.DTO.*;
import com.meta.*;
import com.strava.servicios.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.ApplicationContext;

@SuppressWarnings("serial")
public class RemoteFacade extends UnicastRemoteObject implements IRemoteFacade {
    private EntrenamientoService entrenamientoService;
    private RetoService retoService;
    private ServicioAutentificacion servicioAutentificacion;
    private static HashMap<String, String> tokensActivos = new HashMap<>();
    private final GoogleAuthClient googleAuthClient;
    private UsuarioService usuarioService;
    private AuthClientMeta metaAuthClient;


    @Override
    public List<String> getUsersActivos() throws RemoteException {
        return new ArrayList<>(tokensActivos.keySet());
    }


	public RemoteFacade(ApplicationContext context) throws RemoteException {
        super();
        this.metaAuthClient = new AuthClientMeta("localhost", 1101);
        this.googleAuthClient = context.getBean(GoogleAuthClient.class); //Inyectar el GoogleAuthClient desde el contexto de Spring
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
        return UsuarioService.getUsuarios();
    }



    @Override
    public UsuarioDTO registrarUsuario(String username, String password, String email, String nombre, String proveedor) throws RemoteException {
        UsuarioDTO usuario = UsuarioService.getUsuarios().values().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);

        if (usuario != null) {
            System.out.println("Usuario ya existe: " + username);
            return null;
        }

        return usuarioService.registrar(username, password, email, nombre, proveedor);
    }

    //Metodo login
    public UsuarioDTO login(String username, String contrasena, String plataforma) throws IOException, RemoteException {
        System.out.println("Usuario que quiere iniciar sesión: " + username);

        if (tokensActivos.containsKey(username)) {
            System.out.println("Usuario ya loggeado.");
            return null;
        }

        UsuarioDTO user = usuarioService.obtenerUsuarioPorNombre(username);

        if (plataforma.equalsIgnoreCase("Strava")) {
            if (user == null) {
                System.err.println("No encontrado: " + username);
                return null;
            }
            if (!user.getContrasena().equals(contrasena)) {
                System.err.println("Credenciales inválidas: " + username);
                return null;
            }
            String token = servicioAutentificacion.autenticar(username, contrasena, "Strava");
            if (token == null) {
            	System.err.println("Autenticación fallida: " + username);
                return null;
            }
            if (!user.getProveedor().equalsIgnoreCase("Strava")){
            	System.err.println("Inicio de sesion fallido con strava para : " + username);
                return null;
            }

            user.setToken(token);
            usuarioService.actualizarUsuario(user);
            tokensActivos.put(username, token);
            UsuarioService.getUsuarios().put(user.getId(), user);
            System.out.println("Login exitoso para: " + username);
            return user;
        }

        String token = null;
        if (plataforma.equalsIgnoreCase("Google")) {
            for (UsuarioDTO u : UsuarioService.getUsuarios().values()) {
                if (u.getUsername().equals(username) && u.getContrasena().equals(contrasena)) {
                    if(null != googleAuthClient.loginUser(username, contrasena)) {
                    	token= servicioAutentificacion.autenticar(username, contrasena, "Google");
                        if(token==null) {
                        	return null;
                        }
                        tokensActivos.put(u.getUsername(), token);
                        u.setToken(token);
                        actualizarUsuario(u);
                        return usuarioService.obtenerUsuarioPorNombre(username);
                }
            }

            List<Usuario> usuariosG = googleAuthClient.allUsers();
            for (Usuario usuarioG : usuariosG) {
                if (usuarioG.getUsername().equalsIgnoreCase(username) && usuarioG.getContrasena().equals(contrasena)) {
                    if(null!= googleAuthClient.loginUser(username, contrasena)) {
                    	token= servicioAutentificacion.autenticar(username, contrasena, "Google");
                        if(token==null) {
                        	return null;
                        }
                        UsuarioDTO newUser = usuarioService.registrar(username, contrasena, username + "@google.com", username, "Google");
                        newUser.setToken(token);
                        tokensActivos.put(newUser.getUsername(), token);
                        actualizarUsuario(newUser);
                       
                        return this.getUsuarioService().obtenerUsuarioPorNombre(username);
                    } 
                }
                }
            }
        } else if (plataforma.equalsIgnoreCase("Meta")) {
            for (UsuarioDTO u : UsuarioService.getUsuarios().values()) {
                if (u.getUsername().equals(username) && u.getContrasena().equals(contrasena) && u.getProveedor().equals(plataforma)) {
                    if(null!= metaAuthClient.login(username, contrasena)) { //viejo
	                    token = servicioAutentificacion.autenticar(username, contrasena, "Meta");
	                    
	                    u.setToken(token);
	                    usuarioService.actualizarUsuario(u);
	                    UsuarioDTO usu= usuarioService.obtenerUsuarioPorNombre(username);
	                    tokensActivos.put(usu.getUsername(), token);
	                    return usu;
                    }
                }
            }

            Map<String, String> userStore = metaAuthClient.getUserStore();
            if (userStore.containsKey(username) && userStore.get(username).equals(contrasena)) {
                if(null!= metaAuthClient.login(username, contrasena)) {
	                token = servicioAutentificacion.autenticar(username, contrasena, "Meta");
	                UsuarioDTO newUser = usuarioService.registrar(username, contrasena, username + "@meta.com", username, "Meta");
	                newUser.setToken(token);
	                usuarioService.actualizarUsuario(newUser);
	                UsuarioDTO u= usuarioService.obtenerUsuarioPorNombre(username);
	                tokensActivos.put(u.getUsername(), token);
	                
	                return u;
                }
            }
        }

        System.err.println("Autenticación fallida: " + username + " con plataforma " + plataforma);
        return null;
    }



    @Override
    public void logout(String username) throws RemoteException {
    	UsuarioDTO usuario = usuarioService.obtenerUsuarioPorNombre(username);
        if (usuario != null) {
            String token = usuario.getToken();
            String proveedor = usuario.getProveedor();

            if ("Google".equals(proveedor)) {
                tokensActivos.remove(username);
                usuario.setToken(null);
                actualizarUsuario(usuario);
            } else if ("Meta".equals(proveedor)) {
                try {
                    AuthClientMeta metaAuthClient = new AuthClientMeta("localhost", 1101);
                    metaAuthClient.sendRequest("LOGOUT;" + username);
                    tokensActivos.remove(username);
                    usuario.setToken(null);
                    actualizarUsuario(usuario);
                    System.out.println("Logout realizado correctamente en Meta.");
                } catch (IOException e) {
                    System.err.println("Error durante el logout en Meta: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                usuarioService.logout(token);
                tokensActivos.remove(username);
                usuario.setToken(null);
                actualizarUsuario(usuario);
            }

            System.out.println("Logout completo para el usuario: " + username);
        } else {
            System.out.println("Usuario no encontrado para logout: " + username);
        }
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
    public void cambiarEstado(UsuarioDTO usuario, RetoDTO reto, String estado) throws RemoteException {
    	try {
    		retoService.cambiarEstado(usuario, reto, estado);
    	} catch (Exception e) {
            System.out.println("No disponible");
        }
        

    }

    @Override
    public void actualizarReto(RetoDTO reto, String nombre, LocalDateTime fechaIni, LocalDateTime fechaFin, float distancia,
                               float tiempo, String usuarioCreador, String deporte, ArrayList<Integer> participantes) throws RemoteException {
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




}