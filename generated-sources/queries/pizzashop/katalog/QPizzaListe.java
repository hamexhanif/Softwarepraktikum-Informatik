package pizzashop.katalog;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPizzaListe is a Querydsl query type for PizzaListe
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPizzaListe extends EntityPathBase<PizzaListe> {

    private static final long serialVersionUID = 145882716L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPizzaListe pizzaListe = new QPizzaListe("pizzaListe");

    public final org.salespointframework.catalog.QProduct _super;

    public final EnumPath<PizzaListe.Backgrad> backgrad = createEnum("backgrad", PizzaListe.Backgrad.class);

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

    public final ListPath<Bestelleinheit, QBestelleinheit> zutatenListe = this.<Bestelleinheit, QBestelleinheit>createList("zutatenListe", Bestelleinheit.class, QBestelleinheit.class, PathInits.DIRECT2);

    public QPizzaListe(String variable) {
        this(PizzaListe.class, forVariable(variable), INITS);
    }

    public QPizzaListe(Path<? extends PizzaListe> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPizzaListe(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPizzaListe(PathMetadata metadata, PathInits inits) {
        this(PizzaListe.class, metadata, inits);
    }

    public QPizzaListe(Class<? extends PizzaListe> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new org.salespointframework.catalog.QProduct(type, metadata, inits);
        this.categories = _super.categories;
        this.metric = _super.metric;
        this.name = _super.name;
        this.price = _super.price;
        this.productIdentifier = _super.productIdentifier;
    }

}

