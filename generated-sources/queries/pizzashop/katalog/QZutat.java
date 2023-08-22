package pizzashop.katalog;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QZutat is a Querydsl query type for Zutat
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QZutat extends EntityPathBase<Zutat> {

    private static final long serialVersionUID = -1860635153L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QZutat zutat = new QZutat("zutat");

    public final QBestelleinheit _super;

    //inherited
    public final StringPath bestellType;

    //inherited
    public final SetPath<String, StringPath> categories;

    //inherited
    public final EnumPath<org.salespointframework.quantity.Metric> metric;

    //inherited
    public final StringPath name;

    //inherited
    public final SimplePath<javax.money.MonetaryAmount> price;

    // inherited
    public final org.salespointframework.catalog.QProductIdentifier productIdentifier;

    public QZutat(String variable) {
        this(Zutat.class, forVariable(variable), INITS);
    }

    public QZutat(Path<? extends Zutat> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QZutat(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QZutat(PathMetadata metadata, PathInits inits) {
        this(Zutat.class, metadata, inits);
    }

    public QZutat(Class<? extends Zutat> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QBestelleinheit(type, metadata, inits);
        this.bestellType = _super.bestellType;
        this.categories = _super.categories;
        this.metric = _super.metric;
        this.name = _super.name;
        this.price = _super.price;
        this.productIdentifier = _super.productIdentifier;
    }

}

