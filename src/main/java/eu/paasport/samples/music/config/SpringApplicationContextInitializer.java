package eu.paasport.samples.music.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import eu.paasport.portability.Cloud;
import eu.paasport.portability.CloudException;
import eu.paasport.portability.CloudFactory;
import eu.paasport.portability.service.ServiceInfo;
import eu.paasport.portability.service.common.MysqlServiceInfo;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.*;

public class SpringApplicationContextInitializer implements ApplicationContextInitializer<AnnotationConfigWebApplicationContext> {

    private static final Log logger = LogFactory.getLog(SpringApplicationContextInitializer.class);

    private static final Map<Class<? extends ServiceInfo>, String> serviceTypeToProfileName =
            new HashMap<Class<? extends ServiceInfo>, String>();
    //private static final List<String> validLocalProfiles = Arrays.asList("mysql", "postgres", "mongodb", "redis");
    private static final List<String> validLocalProfiles = Arrays.asList("mysql");

    public static final String IN_MEMORY_PROFILE = "in-memory";

    static {
       // serviceTypeToProfileName.put(MongoServiceInfo.class, "mongodb");
        //serviceTypeToProfileName.put(PostgresqlServiceInfo.class, "postgres");
        serviceTypeToProfileName.put(MysqlServiceInfo.class, "mysql");
        //serviceTypeToProfileName.put(RedisServiceInfo.class, "redis");
       // serviceTypeToProfileName.put(OracleServiceInfo.class, "oracle");
    }

    @Override
    public void initialize(AnnotationConfigWebApplicationContext applicationContext) {
        Cloud cloud = getCloud();

        ConfigurableEnvironment appEnvironment = applicationContext.getEnvironment();

        String[] persistenceProfiles = getCloudProfile(cloud);
        if (persistenceProfiles == null) {
            persistenceProfiles = getActiveProfile(appEnvironment);
        }
        if (persistenceProfiles == null) {
           // persistenceProfiles = new String[] { IN_MEMORY_PROFILE };
            persistenceProfiles = new String[] { "mysql-cloud" };
        }

        for (String persistenceProfile : persistenceProfiles) {
            appEnvironment.addActiveProfile(persistenceProfile);
        }
    }

    public String[] getCloudProfile(Cloud cloud) {
        if (cloud == null) {
            System.out.println(":::NOT CLOUD Found");
            return null;
        }

        List<String> profiles = new ArrayList<String>();

        List<ServiceInfo> serviceInfos = cloud.getServiceInfos();
        System.out.println("cloudsss:::"+cloud.getCloudProperties().stringPropertyNames());
        System.out.println("cloud.application.connection.host:::"+cloud.getCloudProperties().getProperty("cloud.application.connection.host"));
        System.out.println("Found cloud:::"+StringUtils.collectionToCommaDelimitedString(serviceInfos));
        logger.info("Found serviceInfos: " + StringUtils.collectionToCommaDelimitedString(serviceInfos));

        for (ServiceInfo serviceInfo : serviceInfos) {
            if (serviceTypeToProfileName.containsKey(serviceInfo.getClass())) {
                //profiles.add(serviceTypeToProfileName.get(serviceInfo.getClass()));
                profiles.add("mysql");
            }
        }

        if (profiles.size() > 1) {
            throw new IllegalStateException(
                    "Only one service of the following types may be bound to this application: " +
                            serviceTypeToProfileName.values().toString() + ". " +
                            "These services are bound to the application: [" +
                            StringUtils.collectionToCommaDelimitedString(profiles) + "]");
        }

        if (profiles.size() > 0) {
            return createProfileNames(profiles.get(0), "cloud");
        }

        return null;
    }

    private Cloud getCloud() {
        try {
            CloudFactory cloudFactory = new CloudFactory();
            System.out.println("getcloud:::"+cloudFactory.getCloud().toString());
            return cloudFactory.getCloud();
        } catch (CloudException ce) {
            System.out.println("NOCLOUD:::");
            return null;
        }
    }

    private String[] getActiveProfile(ConfigurableEnvironment appEnvironment) {
        List<String> serviceProfiles = new ArrayList<String>();
        System.out.println("profilelist:::"+serviceProfiles.toString());

        for (String profile : appEnvironment.getActiveProfiles()) {
            if (validLocalProfiles.contains(profile)) {
                System.out.println("profileeeeee:::"+profile);
                serviceProfiles.add(profile);
            }
        }

        if (serviceProfiles.size() > 1) {
            throw new IllegalStateException("Only one active Spring profile may be set among the following: " +
                    validLocalProfiles.toString() + ". " +
                    "These profiles are active: [" +
                    StringUtils.collectionToCommaDelimitedString(serviceProfiles) + "]");
        }

        if (serviceProfiles.size() > 0) {
            System.out.println("LOCALprofile");
            return createProfileNames(serviceProfiles.get(0), "local");

        }

        return null;
    }

    private String[] createProfileNames(String baseName, String suffix) {
        String[] profileNames = {baseName, baseName + "-" + suffix};
        logger.info("Setting profile names: " + StringUtils.arrayToCommaDelimitedString(profileNames));
        return profileNames;
    }
}
