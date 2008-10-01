package de.objectcode.time4u.client.connection.impl.ws;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

import de.objectcode.time4u.client.connection.api.ConnectionException;
import de.objectcode.time4u.client.connection.api.IConnection;
import de.objectcode.time4u.client.connection.impl.ws.down.ReceiveProjectChangesCommand;
import de.objectcode.time4u.client.connection.impl.ws.up.SendProjectChangesCommand;
import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.server.api.IConstants;
import de.objectcode.time4u.server.api.ILoginService;
import de.objectcode.time4u.server.api.IPingService;
import de.objectcode.time4u.server.api.IProjectService;
import de.objectcode.time4u.server.api.IRevisionService;
import de.objectcode.time4u.server.api.ITaskService;
import de.objectcode.time4u.server.api.data.Person;
import de.objectcode.time4u.server.api.data.PingResult;
import de.objectcode.time4u.server.api.data.RegistrationInfo;
import de.objectcode.time4u.server.api.data.RevisionStatus;
import de.objectcode.time4u.server.api.data.ServerConnection;
import de.objectcode.time4u.server.utils.DefaultPasswordEncoder;
import de.objectcode.time4u.server.utils.IPasswordEncoder;

public class WSConnection implements IConnection
{
  private final List<ISynchronizationCommand> m_synchronizationCommands;

  private final ServerConnection m_serverConnection;
  private final IPingService m_pingService;
  private final ILoginService m_loginService;
  private final IRevisionService m_revisionService;
  private final IProjectService m_projectService;
  private final ITaskService m_taskService;

  public WSConnection(final ServerConnection serverConnection) throws ConnectionException
  {
    // TODO: This may either be static or configurable
    m_synchronizationCommands = new ArrayList<ISynchronizationCommand>();
    m_synchronizationCommands.add(new SendProjectChangesCommand());
    m_synchronizationCommands.add(new ReceiveProjectChangesCommand());

    m_serverConnection = serverConnection;

    m_pingService = getServicePort("PingService", IPingService.class, false);
    m_loginService = getServicePort("LoginService", ILoginService.class, false);
    m_revisionService = getServicePort("RevisionService", IRevisionService.class, true);
    m_projectService = getServicePort("ProjectService", IProjectService.class, true);
    m_taskService = getServicePort("TaskService", ITaskService.class, true);
  }

  public boolean testConnection() throws ConnectionException
  {
    try {
      final PingResult pingResult = m_pingService.ping();

      return pingResult.getApiVersionMajor() == IConstants.API_VERSION_MAJOR;
    } catch (final Exception e) {
      throw new ConnectionException(e.toString(), e);
    }
  }

  public boolean checkLogin(final Map<String, String> credentials) throws ConnectionException
  {
    try {
      return m_loginService.checkLogin(credentials.get("userId"));
    } catch (final Exception e) {
      throw new ConnectionException(e.toString(), e);
    }
  }

  public RevisionStatus getRevisionStatus() throws ConnectionException
  {
    try {
      return m_revisionService.getRevisionStatus();
    } catch (final Exception e) {
      throw new ConnectionException(e.toString(), e);
    }
  }

  public boolean registerLogin(final Map<String, String> credentials) throws ConnectionException
  {
    try {
      final IPasswordEncoder encoder = new DefaultPasswordEncoder();

      final Person owner = RepositoryFactory.getRepository().getOwner();
      final RegistrationInfo registrationInfo = new RegistrationInfo();

      registrationInfo.setClientId(RepositoryFactory.getRepository().getClientId());
      registrationInfo.setPersonId(owner.getId());
      registrationInfo.setName(owner.getName());
      registrationInfo.setEmail(owner.getEmail());
      registrationInfo.setUserId(credentials.get("userId"));
      registrationInfo.setHashedPassword(encoder.encrypt(credentials.get("password").toCharArray()));

      return m_loginService.registerLogin(registrationInfo);
    } catch (final Exception e) {
      throw new ConnectionException(e.toString(), e);
    }
  }

  private <T> T getServicePort(final String serviceName, final Class<T> portInterface, final boolean secure)
      throws ConnectionException
  {
    final URL wsdl = getClass().getResource(serviceName + ".wsdl");
    final Service service = Service.create(wsdl, new QName("http://objectcode.de/time4u/api/ws", serviceName
        + "WSService"));

    final T port = service.getPort(portInterface);

    final BindingProvider bp = (BindingProvider) port;
    bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
        m_serverConnection.getUrl() + "/time4u-ws" + (secure ? "/secure/" : "/") + serviceName);
    if (secure) {
      final Map<String, String> credentials = m_serverConnection.getCredentials();
      bp.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, credentials.get("userId"));
      bp.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, credentials.get("password"));
    }

    return port;
  }

  public void sychronizeNow() throws ConnectionException
  {
    try {
      final SynchronizationContext context = new SynchronizationContext(RepositoryFactory.getRepository(),
          m_serverConnection.getId(), m_revisionService, m_projectService, m_taskService);

      for (final ISynchronizationCommand command : m_synchronizationCommands) {
        if (command.shouldRun(context)) {
          command.execute(context);
        }
      }
    } catch (final Exception e) {
      throw new ConnectionException(e.toString(), e);
    }

    // TODO: Update lastSynchronized here
  }

  public static void main(final String[] args)
  {
    try {
      final ServerConnection serverConnection = new ServerConnection();

      final Map<String, String> cred = new HashMap<String, String>();
      cred.put("userId", "admin");
      cred.put("password", "admin");
      serverConnection.setCredentials(cred);
      serverConnection.setUrl("http://localhost:8080");
      final WSConnection con = new WSConnection(serverConnection);

      System.out.println(">>" + con.testConnection());

      System.out.println(">" + con.getRevisionStatus());
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }
}
