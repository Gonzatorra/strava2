package com.strava.fachada;

import com.google.server.GoogleAuthClient;
import com.google.server.Usuario;
import com.strava.DTO.*;
import com.meta.*;
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
import org.springframework.stereotype.Component;

@Component
public class RemoteFacade extends UnicastRemoteObject implements IRemoteFacade {

    public UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;//objeto de proyecto google
    public RemoteAuthFacadeMeta remoteAuthFacadeMeta;//objetos de proyecto meta
    private EntrenamientoService entrenamientoService;
    private RetoService retoService;
    private ServicioAutentificacion servicioAutentificacion;
    private static HashMap<String, String> tokensActivos = new HashMap<>();
    private GoogleAuthClient googleAuthClient;


    public RemoteFacade(UsuarioRepository usuarioRepository) throws RemoteException {
        super();
        this.usuarioRepository = usuarioRepository;  // Repositorio de usuarios
        this.googleAuthClient = new GoogleAuthClient(usuarioRepository);  // Inicialización de GoogleAuthClient
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
            u.setToken(servicioAutentificacion.autenticar(username, contrasena, "Strava", u.getProveedor()));
            usuarioService.actualizarUsuario(u);
            UsuarioDTO usu2= UsuarioService.getUsuarios().get(u.getId());
            tokensActivos.put(usu2.getUsername(), usu2.getToken());
            return usu;
        }
        return null;


    }

    @Override
    public UsuarioDTO loginConProveedor(String username, String password, String plataforma) throws RemoteException {
        String token = null;
        UsuarioDTO usuario = usuarioService.obtenerUsuarioPorNombre(username);

        if (usuario != null) {
	        
	        String proveedor = usuario.getProveedor();
	
	        //Verificar si la plataforma del usuario coincide con la proporcionada
	        if (plataforma.equalsIgnoreCase(proveedor)) {
	            //Verificacion para Google
	            if ("Google".equalsIgnoreCase(plataforma)) {
	                token = googleAuthClient.loginUser(username, password);
	                if (token != null) {
	                    System.out.println("Login realizado correctamente en Google.");
	                    //usuarioService.registrar(username, password, username+"@google.com", token, "Google");
	                    
	                } else {
	                    System.err.println("Error en el login de Google.");
	                }
	
	            }
	            //Verificacion para Meta
	            else if ("Meta".equalsIgnoreCase(plataforma)) {
	                try {
	                    AuthClientMeta metaAuthClient = new AuthClientMeta("localhost", 1101);
	                    token = metaAuthClient.login(username, password);
	                    if (token != null) {
	                        System.out.println("Login realizado correctamente en Meta.");
	                        //usuarioService.registrar(username, password, username+"@meta.com", token, "Meta");
	                    } else {
	                        System.err.println("Error durante el login en Meta.");
	                    }
	                } catch (IOException e) {
	                    System.err.println("Error durante el login en Meta: " + e.getMessage());
	                    e.printStackTrace();
	                }
	            }
	
	            //Si se obtiene un token exitoso, actualizar el usuario
	            if (token != null) {
	                usuario.setToken(token); //Establecer el token en el usuario
	                usuarioService.actualizarUsuario(usuario); //Actualizar el usuario en el servicio
	                UsuarioDTO usuarioActualizado = usuarioService.getUsuarios().get(usuario.getId()); //Obtener el usuario actualizado
	                return usuarioActualizado; //Retornar el usuario actualizado
	            } else {
	                System.out.println("No se pudo obtener el token.");
	                return null; //Si no se obtiene un token, retornar null
	            }
	        } else {
	            //Si la plataforma no coincide con el proveedor del usuario, indicar login fallido
	            System.out.println("Login fallido con plataforma: " + plataforma + " para usuario con proveedor: " + proveedor);
	            return null;
	        }
        }
        else {
        	//mirar si existe ese usuario en proveedor
        	//si existe, registrar en strava, asignar token
	        if (plataforma.equalsIgnoreCase("Google")){
	        	Map<String, Usuario> usuariosG = usuarioRepository.getUsuarios();

	        	if(usuariosG.containsKey(username)) {
	        		registrarUsuario(username, password, username+"@google.com", username, "Google");
	        	}
	        }
	        else if (plataforma.equalsIgnoreCase("Meta")){
	        	Map<String, String> userStore = remoteAuthFacadeMeta.getUserStore();
	        	if(userStore.containsKey(username)) {
	        		registrarUsuario(username, password, username+"@meta.com", username, "Meta");
	        	}
	        }
	        
	        
        }
        System.out.println("Ha habido un error desconocido");
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
        UsuarioDTO usuario = usuarioService.obtenerUsuarioPorNombre(username);
        if (usuario != null) {
            String token= usuario.getToken();
            String proveedor = usuario.getProveedor();
            if ("Google".equals(proveedor)) {
                googleAuthClient.logoutUser(username);
            } else if ("Meta".equals(proveedor)) {
                try {
                    AuthClientMeta metaAuthClient = new AuthClientMeta("localhost", 1101);
                    metaAuthClient.sendRequest("LOGOUT;" + username);
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

    @Override
    public UsuarioRepository getUsuarioRepository() throws RemoteException {
        return usuarioRepository;
    }

    public void usarRepositorio() {
        // Usar el repositorio
        System.out.println("Accediendo al repositorio de usuarios: " + usuarioRepository);
    }


}