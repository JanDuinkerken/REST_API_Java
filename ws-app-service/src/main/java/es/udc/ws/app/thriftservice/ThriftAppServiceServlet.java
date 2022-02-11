package es.udc.ws.app.thriftservice;

import es.udc.ws.app.thrift.ThriftAppService;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServlet;

public class ThriftAppServiceServlet extends TServlet {
    public ThriftAppServiceServlet() {
        super(createProcessor(), createProtocolFactory());
    }

    private static TProcessor createProcessor() {

        return new ThriftAppService.Processor<ThriftAppService.Iface>(
                new ThriftAppServiceImpl());

    }

    private static TProtocolFactory createProtocolFactory() {
        return new TBinaryProtocol.Factory();
    }
}
