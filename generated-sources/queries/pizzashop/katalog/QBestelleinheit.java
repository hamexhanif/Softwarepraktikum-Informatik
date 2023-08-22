package pizzashop.katalog;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBestelleinheit is a Querydsl query type for Bestelleinheit
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QBestelleinheit extends EntityPathBase<Bestelleinheit> {

    private static final long serialVersionUID = 467196622L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBestelleinheit bestelleinheit = new QBestelleinheit("bestelleinheit");

    public final org.salespointframework.catalog.QProduct _super;

    public final EnumPath<Bestelleinheit.BestellType> bestellType = createEnum("bestellType", Bestelleinheit.BestellType.class);

    //inherited
    public final SetPath<String, StringPath> categories;

    //inherited
    public final EnumPath<org.salespointframework.quantity.Metric> metric;

    //inherited
    public final StringPath name;

    public final QPizzaListe pizza;

    //inherited
    public final SimplePath<javax.money.MonetaryAmount> price;

    // inherited
    public final org.salespointframework.catalog.QProductIdentifier productIdentifier;

    public QBestelleinheit(String variable) {
        this(Bestelleinheit.class, forVariable(variable), INITS);
    }

    public QBestelleinheit(Path<? extends Bestelleinheit> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBestelleinheit(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBestelleinheit(PathMetadata metadata, PathInits inits) {
        this(Bestelleinheit.class, metadata, inits);
    }

    public QBestelleinheit(Class<? extends Bestelleinheit> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new org.salespointframework.catalog.QProduct(type, metadata, inits);
        this.categories = _super.categories;
        this.metric = _super.metric;
        this.name = _super.name;
        this.pizza = inits.isInitialized("pizza") ? new QPizzaListe(forProperty("pizza"), inits.get("pizza")) : null;
        this.price = _super.price;
        this.productIdentifier = _super.productIdentifier;
    }

}

