package com.devtty.geotest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;

/**
 *
 * @author Denis Renning <denis@devtty.de>
 */
public class GeoTest {

    public static void main(String[] args){
        GeoTest t = new GeoTest();
        t.retrieve();
    }
    
    private void retrieve(){
          
        try {
            
            String getCapabilities = "http://www.geoproxy.geoportal-th.de/geoproxy/services?SERVICE=WFS&VERSION=1.1.0";

            Map connectionParameters = new HashMap();
            connectionParameters.put("WFSDataStoreFactory:GET_CAPABILITIES_URL", getCapabilities);
            connectionParameters.put("WFSDataStoreFactory:TIMEOUT", 10000);
            
            DataStore data = DataStoreFinder.getDataStore(connectionParameters);
            
            String typeNames[] = data.getTypeNames();
            
            for(String s : typeNames){
                System.out.println("asdf: " + s);    
            }

            String typeName = "tlvermgeo:GAZHKO_COMMUNE_FREE";
            
            SimpleFeatureType schema = data.getSchema(typeName);
           
            for(AttributeDescriptor desc : schema.getAttributeDescriptors()){
                System.out.println("AttributeDesc: " + desc.getLocalName());
            }
            
            FeatureSource<SimpleFeatureType, SimpleFeature>  source = data.getFeatureSource(typeName);
            
            FilterFactory ff = CommonFactoryFinder.getFilterFactory();
            
            PropertyName propertyName = ff.property("tlvermgeo:gemeindename");
            Literal literal = ff.literal("Magdala");
            PropertyIsEqualTo filter = ff.equals(propertyName, literal);
                        
            Query query = new Query(typeName, filter);
            
            FeatureCollection<SimpleFeatureType, SimpleFeature> features = source.getFeatures( query );
            
            //ReferencedEnvelope bounds = new ReferencedEnvelope();
            FeatureIterator<SimpleFeature> iterator = features.features();
            
                while( iterator.hasNext() ){
                    Feature feature = (Feature) iterator.next();
                    System.out.println(feature.toString());
                    System.out.println("GK_ " + feature.getProperty("gemkennzahl").toString());
                }
                
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
