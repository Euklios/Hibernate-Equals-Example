package example;

import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EqualsTest {

    @Test
    void testPojoWithGetterAccess() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ExamplePojoWithGetterAccess objInstance = new ExamplePojoWithGetterAccess();
        objInstance.setTestString("thing");

        ExamplePojoWithGetterAccess proxyInstance = createMockInstance(objInstance, ExamplePojoWithGetterAccess.class);

        // This will succeed tue to getter usage
        assertEquals(proxyInstance, objInstance);
        assertEquals(objInstance, proxyInstance);
    }

    @Test
    void testPojoWithPropertyAccess() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ExamplePojoWithPropertyAccess objInstance = new ExamplePojoWithPropertyAccess();
        objInstance.setTestString("thing");

        ExamplePojoWithPropertyAccess proxyInstance = createMockInstance(objInstance, ExamplePojoWithPropertyAccess.class);

        // This will fail tue to property access
        assertEquals(proxyInstance, objInstance);
        assertEquals(objInstance, proxyInstance);
    }

    <T> T createMockInstance(T objInstance, Class<T> objClass) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // Create a new ProxyFactory and set the superclass to the class under test
        final var factory = new ProxyFactory();
        factory.setSuperclass(objClass);

        // Handle all method calls except the 'equals' method itself
        // Should be a more sophisticated check but is sufficient for demonstration
        factory.setFilter(new MethodFilter() {
            @Override
            public boolean isHandled(final Method method) {
                return !method.getName().equals("equals");
            }
        });

        // Redirect all calls to objInstance (except for equals, see above)
        // This means for equals methods using getters, the assertEquals above is more like `assertEquals(objInstance, objInstance)`
        MethodHandler handler = new MethodHandler() {
            @Override
            public Object invoke(final Object self, final Method thisMethod, final Method proceed, final Object[] args)
                    throws Throwable {
                return thisMethod.invoke(objInstance, args);
            }
        };

        // Create a new object using the default constructor and cast it to the objClass
        // In this implementation, the resulting object will only contain null values (as properties)
        // This means property access will only see null, while getters are handled in the MethodHandler above
        return objClass.cast(factory.create(new Class<?>[0], new Object[0], handler));
    }
}
