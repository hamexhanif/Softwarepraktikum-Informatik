package pizzashop.bestellung;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBestellPizza is a Querydsl query type for BestellPizza
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QBestellPizza extends EntityPathBase<BestellPizza> {

    private static final long serialVersionUID = -1929790024L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBestellPizza bestellPizza = new QBestellPizza("bestellPizza");

    public final org.salespointframework.catalog.QProduct _super;

    //inherited
    public final SetPath<String, StringPath> categories;

    //inherited
    public final EnumPath<org.salespointframework.quantity.Metric> metric;

    public final StringPath name = createString("name");

    public final org.salespointframework.catalog.QProductIdentifier pizzaId;

    public final SimplePath<javax.money.MonetaryAmount> preis = createSimple("preis", javax.money.MonetaryAmount.class);

    //inherited
    public final SimplePath<javax.money.MonetaryAmount> price;

    // inherited
    public final org.salespointframework.catalog.QProductIdentifier productIdentifier;

    public final EnumPath<pizzashop.katalog.Bestelleinheit.BestellType> typ = createEnum("typ", pizzashop.katalog.Bestelleinheit.BestellType.class);

    public QBestellPizza(String variable) {
        this(BestellPizza.class, forVariable(variable), INITS);
    }

    public QBestellPizza(Path<? extends BestellPizza> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBestellPizza(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBestellPizza(PathMetadata metadata, PathInits inits) {
        this(BestellPizza.class, metadata, inits);
    }

    public QBestellPizza(Class<? extends BestellPizza> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new org.salespointframework.catalog.QProduct(type, metadata, inits);
        this.categories = _super.categories;
        this.metric = _super.metric;
        this.pizzaId = inits.isInitialized("pizzaId") ? new org.salespointframework.catalog.QProductIdentifier(forProperty("pizzaId")) : null;
        this.price = _super.price;
        this.productIdentifier = _super.productIdentifier;
    }

}

