package kotlin.io.path;

import androidx.tracing.Trace$$ExternalSyntheticApiModelOutline0;
import java.nio.file.Path;
import java.nio.file.Paths;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

@Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\bÂ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0016\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0004R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u0006\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006\n"}, d2 = {"Lkotlin/io/path/PathRelativizer;", "", "()V", "emptyPath", "Ljava/nio/file/Path;", "kotlin.jvm.PlatformType", "parentPath", "tryRelativeTo", "path", "base", "kotlin-stdlib-jdk7"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* compiled from: PathUtils.kt */
final class PathRelativizer {
    public static final PathRelativizer INSTANCE = new PathRelativizer();
    private static final Path emptyPath = Paths.get("", new String[0]);
    private static final Path parentPath = Paths.get("..", new String[0]);

    private PathRelativizer() {
    }

    public final Path tryRelativeTo(Path path, Path path2) {
        Intrinsics.checkNotNullParameter(path, "path");
        Intrinsics.checkNotNullParameter(path2, "base");
        Path m = Trace$$ExternalSyntheticApiModelOutline0.m(path2);
        Path m2 = Trace$$ExternalSyntheticApiModelOutline0.m(path);
        Path m3 = Trace$$ExternalSyntheticApiModelOutline0.m(m, m2);
        int min = Math.min(Trace$$ExternalSyntheticApiModelOutline0.m(m), Trace$$ExternalSyntheticApiModelOutline0.m(m2));
        int i = 0;
        while (i < min) {
            int i2 = i + 1;
            Path m4 = m.getName(i);
            Path path3 = parentPath;
            if (!Intrinsics.areEqual((Object) m4, (Object) path3)) {
                break;
            } else if (Intrinsics.areEqual((Object) m2.getName(i), (Object) path3)) {
                i = i2;
            } else {
                throw new IllegalArgumentException("Unable to compute relative path");
            }
        }
        if (Intrinsics.areEqual((Object) m2, (Object) m) || !Intrinsics.areEqual((Object) m, (Object) emptyPath)) {
            String obj = m3.toString();
            String m5 = Trace$$ExternalSyntheticApiModelOutline0.m(m3).getSeparator();
            Intrinsics.checkNotNullExpressionValue(m5, "rn.fileSystem.separator");
            m2 = StringsKt.endsWith$default(obj, m5, false, 2, (Object) null) ? Trace$$ExternalSyntheticApiModelOutline0.m(m3).getPath(StringsKt.dropLast(obj, Trace$$ExternalSyntheticApiModelOutline0.m(m3).getSeparator().length()), new String[0]) : m3;
        }
        Intrinsics.checkNotNullExpressionValue(m2, "r");
        return m2;
    }
}
