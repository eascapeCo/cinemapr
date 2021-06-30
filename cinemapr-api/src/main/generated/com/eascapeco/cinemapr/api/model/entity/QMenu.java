package com.eascapeco.cinemapr.api.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMenu is a Querydsl query type for Menu
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMenu extends EntityPathBase<Menu> {

    private static final long serialVersionUID = -1026862661L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMenu menu = new QMenu("menu");

    public final NumberPath<Integer> dpSequence = createNumber("dpSequence", Integer.class);

    public final BooleanPath dpYn = createBoolean("dpYn");

    public final NumberPath<Integer> mnuLv = createNumber("mnuLv", Integer.class);

    public final StringPath mnuName = createString("mnuName");

    public final NumberPath<Long> mnuNo = createNumber("mnuNo", Long.class);

    public final DateTimePath<java.time.LocalDateTime> modDate = createDateTime("modDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> modNo = createNumber("modNo", Integer.class);

    public final QMenu parentMenu;

    public final DateTimePath<java.time.LocalDateTime> regDate = createDateTime("regDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> regNo = createNumber("regNo", Integer.class);

    public final ListPath<Menu, QMenu> subMenus = this.<Menu, QMenu>createList("subMenus", Menu.class, QMenu.class, PathInits.DIRECT2);

    public final StringPath urlAdr = createString("urlAdr");

    public final BooleanPath useYn = createBoolean("useYn");

    public QMenu(String variable) {
        this(Menu.class, forVariable(variable), INITS);
    }

    public QMenu(Path<? extends Menu> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMenu(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMenu(PathMetadata metadata, PathInits inits) {
        this(Menu.class, metadata, inits);
    }

    public QMenu(Class<? extends Menu> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.parentMenu = inits.isInitialized("parentMenu") ? new QMenu(forProperty("parentMenu"), inits.get("parentMenu")) : null;
    }

}

