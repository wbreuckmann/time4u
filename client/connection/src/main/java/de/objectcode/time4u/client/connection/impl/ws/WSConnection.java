package de.objectcode.time4u.client.connection.impl.ws;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

import de.objectcode.time4u.client.connection.api.ConnectionException;
import de.objectcode.time4u.client.connection.api.IConnection;
import de.objectcode.time4u.server.api.IConstants;
import de.objectcode.time4u.server.api.ILoginService;
import de.objectcode.time4u.server.api.IPingService;
import de.objectcode.time4u.server.api.data.PingResult;
import de.objectcode.time4u.server.utils.DefaultPasswordEncoder;
import de.objectcode.time4u.server.utils.IPasswordEncoder;

public class WSConnection implements IConnection
{
  IPingService m_pingService;
  ILoginService m_loginService;

  public WSConnection(final URL url, final Map<String, String> credentials) throws ConnectionException
  {
    m_pingService = getServicePort(url, "PingService", IPingService.class);
    m_loginService = getServicePort(url, "LoginService", ILoginService.class);
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

  public boolean registerLogin(final Map<String, String> credentials) throws ConnectionException
  {
    try {
      final IPasswordEncoder encoder = new DefaultPasswordEncoder();

      return m_loginService.registerLogin(credentials.get("userId"), encoder.encrypt(credentials.get("password")
          .toCharArray()), credentials.get("userId"), "");
    } catch (final Exception e) {
      throw new ConnectionException(e.toString(), e);
    }
  }

  private <T> T getServicePort(final URL baseUrl, final String serviceName, final Class<T> portInterface)
      throws ConnectionException
  {
    final URL wsdl = getClass().getResource(serviceName + ".wsdl");
    final Service service = Service.create(wsdl, new QName("http://objectcode.de/time4u/api/ws", serviceName
        + "WSService"));

    final T port = service.getPort(portInterface);

    try {
      final BindingProvider bp = (BindingProvider) port;
      bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
          new URL(baseUrl, "/time4u-ws/" + serviceName).toString());
    } catch (final MalformedURLException e) {
      throw new ConnectionException("Malformed url", e);
    }

    return port;
  }

  public static void main(final String[] args)
  {
    try {
      final Map<String, String> cred = new HashMap<String, String>();

      final WSConnection con = new WSConnection(new URL("http://localhost:8080"), cred);

      System.out.println(">>" + con.testConnection());
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }
}
