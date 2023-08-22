package pizzashop.personal;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPersonal is a Querydsl query type for Personal
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPersonal extends EntityPathBase<Personal> {

    private static final long serialVersionUID = 182030302L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPersonal personal = new QPersonal("personal");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath job = createString("job");

    public final org.salespointframework.useraccount.QUserAccount userAccount;

    public QPersonal(String variable) {
        this(Personal.class, forVariable(variable), INITS);
    }

    public QPersonal(Path<? extends Personal> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPersonal(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPersonal(PathMetadata metadata, PathInits inits) {
        this(Personal.class, metadata, inits);
    }

    public QPersonal(Class<? extends Personal> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.userAccount = inits.isInitialized("userAccount") ? new org.salespointframework.useraccount.QUserAccount(forProperty("userAccount"), inits.get("userAccount")) : null;
    }

}

