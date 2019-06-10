package org.afc.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.*;

import javax.annotation.Generated;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.DatePath;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;


/**
 * QTrade is a Querydsl query type for Trade
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QTrade extends EntityPathBase<Trade> {

    private static final long serialVersionUID = -67233899L;

    public static final QTrade trade = new QTrade("trade");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<ProductType> product = createEnum("product", ProductType.class);

    public final EnumPath<TradeStatusType> status = createEnum("status", TradeStatusType.class);

    public QTrade(String variable) {
        super(Trade.class, forVariable(variable));
    }

    public QTrade(Path<? extends Trade> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTrade(PathMetadata metadata) {
        super(Trade.class, metadata);
    }

}

