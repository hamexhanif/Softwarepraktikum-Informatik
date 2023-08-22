package pizzashop.ofen;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QOfen is a Querydsl query type for Ofen
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QOfen extends EntityPathBase<Ofen> {

    private static final long serialVersionUID = -1665472802L;

    public static final QOfen ofen = new QOfen("ofen");

    public final BooleanPath free = createBoolean("free");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> startTime = createDateTime("startTime", java.time.LocalDateTime.class);

    public QOfen(String variable) {
        super(Ofen.class, forVariable(variable));
    }

    public QOfen(Path<? extends Ofen> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOfen(PathMetadata metadata) {
        super(Ofen.class, metadata);
    }

}

