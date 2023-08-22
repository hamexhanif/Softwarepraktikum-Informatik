package pizzashop.bestellung;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPizzashopBestellung is a Querydsl query type for PizzashopBestellung
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPizzashopBestellung extends EntityPathBase<PizzashopBestellung> {

    private static final long serialVersionUID = -1143203558L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPizzashopBestellung pizzashopBestellung = new QPizzashopBestellung("pizzashopBestellung");

    public final org.salespointframework.order.QOrder _super;

    //inherited
    public final ListPath<org.salespointframework.order.ChargeLine.AttachedChargeLine, org.salespointframework.order.QChargeLine_AttachedChargeLine> attachedChargeLines;

    //inherited
    public final ListPath<org.salespointframework.order.ChargeLine, org.salespointframework.order.QChargeLine> chargeLines;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> dateCreated;

    public final ListPath<String, StringPath> kundenDaten = this.<String, StringPath>createList("kundenDaten", String.class, StringPath.class, PathInits.DIRECT2);

    public final StringPath lieferbotenId = createString("lieferbotenId");

    // inherited
    public final org.salespointframework.order.QOrderIdentifier orderIdentifier;

    //inherited
    public final ListPath<org.salespointframework.order.OrderLine, org.salespointframework.order.QOrderLine> orderLines;

    //inherited
    public final EnumPath<org.salespointframework.order.OrderStatus> orderStatus;

    public final EnumPath<PizzashopBestellung.OrderType> orderType = createEnum("orderType", PizzashopBestellung.OrderType.class);

    //inherited
    public final SimplePath<org.salespointframework.payment.PaymentMethod> paymentMethod;

    public final StringPath paymentMethodString = createString("paymentMethodString");

    // inherited
    public final org.salespointframework.useraccount.QUserAccount userAccount;

    public QPizzashopBestellung(String variable) {
        this(PizzashopBestellung.class, forVariable(variable), INITS);
    }

    public QPizzashopBestellung(Path<? extends PizzashopBestellung> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPizzashopBestellung(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPizzashopBestellung(PathMetadata metadata, PathInits inits) {
        this(PizzashopBestellung.class, metadata, inits);
    }

    public QPizzashopBestellung(Class<? extends PizzashopBestellung> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new org.salespointframework.order.QOrder(type, metadata, inits);
        this.attachedChargeLines = _super.attachedChargeLines;
        this.chargeLines = _super.chargeLines;
        this.dateCreated = _super.dateCreated;
        this.orderIdentifier = _super.orderIdentifier;
        this.orderLines = _super.orderLines;
        this.orderStatus = _super.orderStatus;
        this.paymentMethod = _super.paymentMethod;
        this.userAccount = _super.userAccount;
    }

}

