package pizzashop.kunde;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QKunde is a Querydsl query type for Kunde
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QKunde extends EntityPathBase<Kunde> {

    private static final long serialVersionUID = -1583003188L;

    public static final QKunde kunde = new QKunde("kunde");

    public final StringPath adresse = createString("adresse");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath nachname = createString("nachname");

    public final StringPath tan = createString("tan");

    public final StringPath telefon = createString("telefon");

    public final StringPath vorname = createString("vorname");

    public QKunde(String variable) {
        super(Kunde.class, forVariable(variable));
    }

    public QKunde(Path<? extends Kunde> path) {
        super(path.getType(), path.getMetadata());
    }

    public QKunde(PathMetadata metadata) {
        super(Kunde.class, metadata);
    }

}

