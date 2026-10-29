"""Gera o diagrama UML (estilo ferramenta de desenho, fundo quadriculado)."""
import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt
from matplotlib.patches import Rectangle, FancyArrowPatch

LH, PAD, NAME_H = 0.52, 0.25, 0.75
W_UN, H_UN = 53, 25

fig, ax = plt.subplots(figsize=(22, 10.8))
ax.set_xlim(0, W_UN); ax.set_ylim(0, H_UN); ax.set_aspect("equal"); ax.axis("off")
for i in range(W_UN + 1):
    ax.plot([i, i], [0, H_UN], color="#E3E3E3", lw=0.6, zorder=0)
for j in range(H_UN + 1):
    ax.plot([0, W_UN], [j, j], color="#E3E3E3", lw=0.6, zorder=0)

def box(cx, top, w, name, stereo=None, attrs=(), methods=()):
    h_name = NAME_H * (2 if stereo else 1) + (0.15 if stereo else 0)
    h_attr = max(len(attrs), 1) * LH + PAD
    h_meth = max(len(methods), 1) * LH + PAD
    h = h_name + h_attr + h_meth
    x0, y0 = cx - w / 2, top - h
    ax.add_patch(Rectangle((x0, y0), w, h, fc="white", ec="black", lw=1.4, zorder=2))
    ax.plot([x0, x0 + w], [top - h_name] * 2, color="black", lw=1.2, zorder=3)
    ax.plot([x0, x0 + w], [top - h_name - h_attr] * 2, color="black", lw=1.2, zorder=3)
    ty = top - 0.45
    if stereo:
        ax.text(cx, ty, f"«{stereo}»", ha="center", va="center", fontsize=9, zorder=4)
        ty -= 0.65
    ax.text(cx, ty, name, ha="center", va="center", fontsize=11, fontweight="bold", zorder=4)
    for k, a in enumerate(attrs):
        ax.text(x0 + 0.25, top - h_name - PAD / 2 - LH * (k + .5), a, ha="left", va="center", fontsize=8.5, zorder=4)
    for k, m in enumerate(methods):
        ax.text(x0 + 0.25, top - h_name - h_attr - PAD / 2 - LH * (k + .5), m, ha="left", va="center", fontsize=8.5, zorder=4)
    return dict(cx=cx, cy=(top + y0) / 2, top=top, bottom=y0, left=x0, right=x0 + w)

def line(pts, dashed=False):
    xs, ys = zip(*pts)
    ax.plot(xs, ys, color="black", lw=1.3, zorder=1, linestyle=(0, (5, 4)) if dashed else "-")

def arrow(p0, p1, style, dashed=False):
    a = FancyArrowPatch(p0, p1, arrowstyle=style, mutation_scale=22 if "|" in style else 18, lw=1.3,
                        color="black", zorder=1, shrinkA=0, shrinkB=0,
                        linestyle=(0, (5, 4)) if dashed else "-")
    if "|" in style:
        a.set_facecolor("white")
    ax.add_patch(a)

CX = 13.95
main = box(CX, 24.4, 5.6, "Main", None, (), ("+ main(args : String[])",))
aud = box(CX, 19.6, 14, "Auditor", "abstract",
          ("# procedimentosAutorizados : List<TipoProcedimento>", "- auditorSuperior : Auditor"),
          ("+ autorizar(solicitacao : Solicitacao) : String",
           "+ getDescricaoCargo() : String  {abstract}",
           "+ getAuditorSuperior() : Auditor",
           "+ setAuditorSuperior(auditor : Auditor)"))

subs = [
    box(3.6, 9.3, 6.6, "AuditorAtendente", None, (), ("+ AuditorAtendente(superior)", "+ getDescricaoCargo() : String")),
    box(10.5, 9.3, 6.6, "AuditorEnfermeiro", None, (), ("+ AuditorEnfermeiro(superior)", "+ getDescricaoCargo() : String")),
    box(17.4, 9.3, 6.6, "AuditorMedico", None, (), ("+ AuditorMedico(superior)", "+ getDescricaoCargo() : String")),
    box(24.3, 9.3, 6.6, "AuditorDiretorClinico", None, (), ("+ AuditorDiretorClinico(superior)", "+ getDescricaoCargo() : String")),
]

sol = box(35, 24.4, 10, "Solicitacao", None,
          ("- tipoProcedimento : TipoProcedimento",),
          ("+ Solicitacao(tipo)", "+ getTipoProcedimento()", "+ setTipoProcedimento(tipo)"))
tp = box(35, 17.2, 9, "TipoProcedimento", "interface", (), ())

nomes = ["Consulta", "Exame", "Cirurgia", "Internacao", "Experimental"]
tipos = []
for k, n in enumerate(nomes):
    tipos.append(box(47.5, 23.4 - k * 4.4, 9.6, f"TipoProcedimento{n}", None,
                     (f"- tipoProcedimento{n}  {{static}}",), (f"+ getTipoProcedimento{n}()",)))

# Main -> Auditor (dependência)
arrow((CX, main["bottom"]), (CX, aud["top"]), "->", dashed=True)
# Main -> Solicitacao (dependência)
arrow((main["right"], main["cy"]), (sol["left"], main["cy"]), "->", dashed=True)
# Auditor -> Solicitacao (dependência)
arrow((aud["right"], aud["cy"]), (sol["left"], sol["cy"] - 0.6), "->", dashed=True)

# autoassociação do Auditor (1)
lx = CX + 4.5
line([(lx, aud["top"]), (lx, aud["top"] + 1.2), (aud["right"] + 1.4, aud["top"] + 1.2),
      (aud["right"] + 1.4, aud["top"] - 1.4)])
arrow((aud["right"] + 1.4, aud["top"] - 1.4), (aud["right"], aud["top"] - 1.4), "->")
ax.text(aud["right"] + 0.2, aud["top"] - 1.05, "1", fontsize=9)

# herança das subclasses
ymid = (subs[0]["top"] + aud["bottom"]) / 2
for s in subs:
    line([(s["cx"], s["top"]), (s["cx"], ymid)])
line([(subs[0]["cx"], ymid), (subs[-1]["cx"], ymid)])
arrow((CX, ymid), (CX, aud["bottom"]), "-|>")

# Solicitacao -> TipoProcedimento (associação 1)
arrow((35, sol["bottom"]), (35, tp["top"]), "->")
ax.text(35.25, tp["top"] + 0.35, "1", fontsize=9)

# realização dos tipos concretos
xt = 42.2
for t in tipos:
    line([(t["left"], t["cy"]), (xt, t["cy"])], dashed=True)
line([(xt, tipos[0]["cy"]), (xt, tipos[-1]["cy"])], dashed=True)
ytp = tp["cy"]
line([(xt, ytp), (tp["right"] + 0.05, ytp)], dashed=True)
arrow((xt - 0.6, ytp), (tp["right"], ytp), "-|>", dashed=True)

plt.savefig("diagrama-uml.png", dpi=140, bbox_inches="tight", facecolor="white")
